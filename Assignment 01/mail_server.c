#include <stdio.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#define PORT 4444

char buffer[100] = {0};
char* append(char *c,char *d)
{
    int cl=strlen(c);
    int dl=strlen(d);
    char *ret=(char *)malloc((cl+dl+1)*sizeof(char));
    int i;
    for(i=0; i<cl; i++)
    {
        ret[i]=c[i];
    }
    for(i=0; i<=dl; i++)
    {
        ret[i+cl]=d[i];

    }
    return ret;
}
char *extractMail(char *c)
{
    char *ret=(char *)malloc(strlen(c)*sizeof(c));
    int i=0,j=0;
    while(c[i]!='<')i++;
    i++;
    while(c[i]!='>')
    {
        ret[j]=c[i];
        i++;
        j++;
    }
    ret[i]=0;
    return ret;
}
void flush()
{
    int i=0;
    for(i=0; i<100; i++)
    {
        buffer[i]=0;
    }
}
int main(int argc, char const *argv[])
{
    printf("Port no: %d\n",PORT);
    flush();
    int server_fd, new_socket, valread;
    struct sockaddr_in address;
    int opt = 1;
    int addrlen = sizeof(address);

    char hostname[1024];
    gethostname(hostname, 1024);



    server_fd = socket(AF_INET, SOCK_STREAM, 0);
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT,
               &opt, sizeof(opt));

    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons( PORT );

    bind(server_fd, (struct sockaddr *)&address,
         sizeof(address));

    listen(server_fd, 3);
    while((new_socket = accept(server_fd, (struct sockaddr *)&address,
                               (socklen_t*)&addrlen))>0)
    {
        flush();
        char *READY = "220 ";
        READY=append(READY,hostname);
        char *HELO="250 Hello ";
        char *HELO2=", pleased to meet you";
        char *SENDER="250 ";
        char *SENDER2="... Sender ok";
        char *RECIPIENT="250 ok ";
        char *DATA_START="354 Enter mail, end with \".\" on a line by itself";
        char *ACCEPTED="250 Message accepted for delivery";
        char *CLOSE_CONNECTION="221 ";
        char *CLOSE_CONNECTION2=" closing connection";
        CLOSE_CONNECTION=append(CLOSE_CONNECTION,hostname);
        CLOSE_CONNECTION=append(CLOSE_CONNECTION,CLOSE_CONNECTION2);
        char *peerIP;
        peerIP=inet_ntoa(address.sin_addr);
         char* IC="500 INVALID COMMAND";

        send(new_socket,READY, strlen(READY), 0 );
        printf("S: %s\r\n",READY );

        flush();
        valread = read( new_socket, buffer, 1024);
        if(buffer[0]!='H'||buffer[1]!='E'||buffer[2]!='L'||buffer[3]!='O'){

            send(new_socket,IC, strlen(IC), 0 );
            printf("S: %s\n%s\r\n\n\n",buffer,IC );
            close(new_socket);
            continue;
        }
        printf("C: %s\r\n",buffer );

        HELO=append(HELO,peerIP);
        HELO=append(HELO,HELO2);
        send(new_socket,HELO, strlen(HELO), 0 );
        printf("S: %s\r\n",HELO );

        flush();
        valread = read( new_socket, buffer, 1024);
        if(buffer[0]!='M'||buffer[1]!='A'||buffer[2]!='I'||buffer[3]!='L'||buffer[4]!=' '||buffer[5]!='F'||buffer[6]!='R'||buffer[7]!='O'||buffer[8]!='M'){

            send(new_socket,IC, strlen(IC), 0 );
            printf("S: %s\n%s\r\n\n\n",buffer,IC );
            close(new_socket);
            continue;
        }
        printf("C: %s\r\n",buffer );

        SENDER=append(SENDER,extractMail(buffer));
        SENDER=append(SENDER,SENDER2);
        send(new_socket,SENDER, strlen(SENDER), 0 );
        printf("S: %s\r\n",SENDER );

        flush();
        valread = read( new_socket, buffer, 1024);
        if(buffer[0]!='R'||buffer[1]!='C'||buffer[2]!='P'||buffer[3]!='T'||buffer[4]!=' '||buffer[5]!='T'||buffer[6]!='O'){

            send(new_socket,IC, strlen(IC), 0 );
            printf("S: %s\r\n%s\r\n\n\n",buffer,IC );
            close(new_socket);
            continue;
        }
        printf("C: %s \r\n",buffer );

        char *remail=extractMail(buffer);
        RECIPIENT=append(RECIPIENT,extractMail(buffer));
        
	
	
	int ind=0;
	for(ind=0;ind<strlen(remail);ind++)
	{
		if(remail[ind]=='@'){
			remail[ind]=0;
			break;
		}
	}
	
        FILE *ppFile;

        ppFile=fopen(remail, "r");
        if(ppFile==NULL)
        {
            char* er="421 User not exists";

            send(new_socket,er, strlen(er), 0 );
            printf("S: %s\r\n\n\n\n",er );
            close(new_socket);
            continue;
        }
        fclose(ppFile);


        send(new_socket,RECIPIENT, strlen(RECIPIENT), 0 );
        printf("S: %s\r\n",RECIPIENT );


        flush();
        valread = read( new_socket, buffer, 1024);
        if(buffer[0]!='D'||buffer[1]!='A'||buffer[2]!='T'||buffer[3]!='A'){

            send(new_socket,IC, strlen(IC), 0 );
            printf("S: %s\r\n%s\r\n\n\n",buffer,IC );
            close(new_socket);
            continue;
        }
        printf("C: %s\r\n",buffer );



        send(new_socket,DATA_START, strlen(DATA_START), 0 );
        printf("S: %s\r\n",DATA_START);

        FILE *pFile;
        pFile=fopen(remail, "a");


        ///
        while(1)
        {
            flush();
            valread = read( new_socket, buffer, 1024);
            printf("C: %s\r\n",buffer);
            if(buffer[0]=='.'&&buffer[1]==0)
                break;
            else
            {

                send(new_socket,".", strlen("."), 0 );
                fprintf(pFile, "%s\r\n", buffer);
            }
        }
        fprintf(pFile, "\r\n\n\n\n" );
        fclose(pFile);

        ///

        send(new_socket,ACCEPTED, strlen(ACCEPTED), 0 );
        printf("S: %s\r\n",ACCEPTED);

        flush();
        valread = read( new_socket, buffer, 1024);
        if(buffer[0]!='Q'||buffer[1]!='U'||buffer[2]!='I'||buffer[3]!='T'){

            send(new_socket,IC, strlen(IC), 0 );
            printf("S: %s\r\n%s\r\n\n\n",buffer,IC );
            close(new_socket);
            continue;
        }
        printf("C: %s\r\n",buffer );

        send(new_socket,CLOSE_CONNECTION, strlen(CLOSE_CONNECTION), 0 );

        printf("S: %s\r\n\n\n\n",CLOSE_CONNECTION);
        close(new_socket);
    }

    return 0;
}


