package main

import (
	"bytes"
	"context"
	"crypto/sha256"
	"fmt"
	"log"
	"net/http"
	"time"

	v4 "github.com/aws/aws-sdk-go-v2/aws/signer/v4"
	"github.com/aws/aws-sdk-go-v2/config"
)

func main() {

	/*
		load shared credentials files from ~/.aws/config,
		sample config file:
		[profile admin]
		region = us-west-1
		service = execute-api
		aws_access_key_id = xxxxxx
		aws_secret_access_key = xxxxxxxxxx

		visit https://docs.aws.amazon.com/sdkref/latest/guide/file-format.html for more details
	*/
	ctx := context.Background()
	cfg, err := config.LoadDefaultConfig(ctx, config.WithSharedConfigProfile("admin"))
	if err != nil {
		log.Fatalf("unable to load SDK config, %v", err)
	}

	// check if credential is valid
	credentials, err := cfg.Credentials.Retrieve(ctx)
	if err != nil {
		log.Fatalf("Credentials.Retrieve err, %v", err)
	}

	apiUrl := "https://<endpoint-url>"
	// Request body
	jsonStr := []byte(`{"To":"<target-phone-number>","Msg":"hello world"}`)
	req, err := http.NewRequest("POST", apiUrl, bytes.NewBuffer(jsonStr))
	if err != nil {
		log.Fatalf("http.NewRequest err, %v", err)
	}

	reqBodyHashBytes := sha256.Sum256(jsonStr)
	reqBodyHashHex := fmt.Sprintf("%x", reqBodyHashBytes)

	signer := v4.NewSigner()
	if err = signer.SignHTTP(ctx, credentials, req, reqBodyHashHex, "execute-api", cfg.Region, time.Now()); err != nil {
		fmt.Printf("Error signing request.\n")
		return
	}
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Println("Error reading response body:", err)
		return
	}
	defer resp.Body.Close()

	fmt.Println("response Status:", resp.Status)
	// Print the response body
	buf := new(bytes.Buffer)
	buf.ReadFrom(resp.Body)
	fmt.Println("response Body:", buf.String())
}
