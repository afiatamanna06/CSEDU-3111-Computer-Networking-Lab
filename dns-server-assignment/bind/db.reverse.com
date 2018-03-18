;
; BIND reverse data file for local loopback interface
;
$TTL	604800
@	IN	SOA	ns.tanvir.com. root.localhost. (
			      1		; Serial
			 604800		; Refresh
			  86400		; Retry
			2419200		; Expire
			 604800 )	; Negative Cache TTL
;
@	IN	NS	ns.
1	IN	PTR	ns.tanvir.com.
2	IN	PTR	server.tanvir.com
3	IN	PTR	smtp.tanvir.com
4	IN	PTR	ns2.tanvir.com
