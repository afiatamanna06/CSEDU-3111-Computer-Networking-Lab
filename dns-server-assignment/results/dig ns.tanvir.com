dig ns.tanvir.com

; <<>> DiG 9.10.3-P4-Ubuntu <<>> ns.tanvir.com
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 18045
;; flags: qr aa rd ra; QUERY: 1, ANSWER: 1, AUTHORITY: 2, ADDITIONAL: 3

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;ns.tanvir.com.			IN	A

;; ANSWER SECTION:
ns.tanvir.com.		604800	IN	A	127.0.0.1

;; AUTHORITY SECTION:
tanvir.com.		604800	IN	NS	ns.tanvir.com.
tanvir.com.		604800	IN	NS	ns2.tanvir.com.

;; ADDITIONAL SECTION:
ns.tanvir.com.		604800	IN	AAAA	::1
ns2.tanvir.com.		604800	IN	A	127.0.0.4

;; Query time: 0 msec
;; SERVER: 127.0.0.1#53(127.0.0.1)
;; WHEN: Sun Mar 18 15:49:51 +06 2018
;; MSG SIZE  rcvd: 134

