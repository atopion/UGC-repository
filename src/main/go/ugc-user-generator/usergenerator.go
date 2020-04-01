package main

import (
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"golang.org/x/crypto/bcrypt"
	"strings"
)

func main() {

	var m string = "898BAFF0B52F5D02DADB9191676FA3BD22929845EAC7ED29940CBBFD20E0D2FE0E8F25FD7397C399415D2A222CE522C29BD5F335FEDCEB19DFC865A6DEF010D7"
	var master_key, err = hex.DecodeString(m)
	if err != nil {
		fmt.Printf("Could not read master key: %v", err)
	}
	fmt.Printf("Using master_key:     %q\n\n", m)

	var secret []byte = make([]byte, 64)
	_, err = rand.Read(secret)
	if err != nil {
		fmt.Printf("Could not generate secret: %v\n", err)
	}
	fmt.Printf("User plain secret:    %q\n", strings.ToUpper(hex.EncodeToString(secret)))

	var key []byte = make([]byte, 64)
	_, err = rand.Read(key)
	if err != nil {
		fmt.Printf("Could not generate user key: %v\n", err)
	}
	fmt.Printf("User key:             %q\n", strings.ToUpper(hex.EncodeToString(key)))

	keyhash, err := bcrypt.GenerateFromPassword(key, 8)
	if err != nil {
		fmt.Printf("Could not generate user key hash: %v\n", err)
	}
	fmt.Printf("User key hash:        %q\n", keyhash)

	var tmp []byte = make([]byte, 64)
	var secret_enc []byte = make([]byte, 64)
	for i := 0; i < 64; i++ {
		tmp[i] = secret[i] ^ key[i]
		secret_enc[i] = tmp[i] ^ master_key[i]
	}
	fmt.Printf("User encypted secret: %q\n", strings.ToUpper(hex.EncodeToString(secret_enc)))

}
