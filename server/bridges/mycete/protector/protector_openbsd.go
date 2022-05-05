// +build openbsd

package protector

import "golang.org/x/sys/unix"

func Protect(s string) {
	unix.PledgePromises(s)
}
