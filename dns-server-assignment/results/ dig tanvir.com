 dig tanvir.com

; <<>> DiG 9.10.3-P4-Ubuntu <<>> tanvir.com
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 2659
;; flags: qr aa rd ra; QUERY: 1, ANSWER: 0, AUTHORITY: 1, ADDITIONAL: 1

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;tanvir.com.			IN	A

;; AUTHORITY SECTION:
tanvir.com.		604800	IN	SOA	ns.tanvir.com. root.localhost. 2 604800 86400 2419200 604800

;; Query time: 0 msec
;; SERVER: 127.0.0.1#53(127.0.0.1)
;; WHEN: Sun Mar 18 15:50:54 +06 2018
;; MSG SIZE  rcvd: 92

