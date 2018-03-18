;
; BIND data file for local loopback interface
;
$TTL	604800
@	IN	SOA	ns.tanvir.com. root.localhost. (
			      2		; Serial
			 604800		; Refresh
			  86400		; Retry
			2419200		; Expire
			 604800 )	; Negative Cache TTL
;
@	IN	NS	ns.tanvir.com.
@	IN	NS	ns2.tanvir.com.
@	IN	MX	10 smtp
ns	IN	A	127.0.0.1
ns	IN	AAAA	::1
ns2	IN	A	127.0.0.4
server	IN	A	127.0.0.2
www	IN	CNAME	server
smtp	IN	A	127.0.0.3
