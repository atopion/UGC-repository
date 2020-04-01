package pkg

import (
	"errors"
	"strings"
)

func StrIndexAt(s, sep string, n int) int {
	idx := strings.Index(s[n:], sep)
	if idx > -1 {
		idx += n
	}
	return idx
}

func StrExtract(s, start, end string) (string, error) {
	idx1 := strings.Index(s, start)
	idx2 := strings.LastIndex(s, end)

	if idx1 == -1 || idx2 == -1 {
		return "", errors.New("Delimiter not found")
	}
	return s[idx1+1 : idx2], nil
}
