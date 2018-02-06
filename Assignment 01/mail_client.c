#include <stdio.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include<time.h>
#include <sys/socket.h>

#include <sys/types.h>

#include <netinet/in.h>

#include <netdb.h>

#include <stdio.h>

#include <string.h>

#include <stdlib.h>

#include <unistd.h>

#include <errno.h>
//#define PORT 4444
int PORT=0;
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

char *getFile(char *fname)
{
    char *ch;
    ch=malloc(250*sizeof(char));
    int c;
    int i=0;
    FILE *file;
    file = fopen(fname, "r");
    if (file)
    {
        while ((c = getc(file)) != EOF)
        {
            ch[i]=c;
            i++;
        }
        fclose(file);
        ch[i]=0;
    }
    else
    {
        printf("FILE NOT FOUND\n");
        exit(0);
    }
    return ch;
}
char *getMyEmail()
{

    char hostname[50];
    gethostname(hostname, 50);
    char *uname=getenv("USER");
    char *email=strcat(uname,"@");
    email= strcat(email,hostname);
    return email;

}

void flush()
{
    int i=0;
    for(i=0; i<100; i++)
    {
        buffer[i]=0;
    }
}
char *getMail(char *c)
{
    char *ret=(char *)malloc(strlen(c)*sizeof(char));
    int i=0;
    while(1)
    {
        ret[i]=c[i];
        if(c[i]==':')
        {
            ret[i]=0;
            return ret;
        }
        i++;
    }
}
char *getServer(char *c)
{

    char *ret=(char *)malloc(strlen(c)*sizeof(char));
    int i=0;
    while(1)
    {
        if(c[i]=='@')
        {
            break;
        }
        i++;
    }
    i++;
    int j=0;
    while(1)
    {
        ret[j]=c[i];
        if(c[i]==':')
        {
            ret[j]=0;
            return ret;
        }
        i++;
        j++;
    }
}
int checkMail(char *c)
{
    int at,col;
    at=0;
    col=0;
    int i=0;
    for(i=0; i<strlen(c); i++)
    {
        if(!at)
        {
            if(c[i]=='@')
                at=1;
        }
        else
        {
            if(c[i]==':')
                col=1;
        }
    }
    if(at&&col)return 1;
    return 0;
}
int getPort(char *c)
{
    char *ret=(char *)malloc(6*sizeof(char));
    int i=0;
    while(1)
    {
        if(c[i]==':')
            break;
        i++;
    }
    i++;
    ret[0]=c[i];
    ret[1]=c[i+1];
    ret[2]=c[i+2];
    ret[3]=c[i+3];
    ret[4]=0;
    return atoi(ret);
}
int main(int argc, char const *argv[])
{
    if(argc<4)
    {
        printf("Incorrect number of arguments : %d\n",argc);
        return 0;
    }
    char *ar=argv[1];
    if(checkMail(ar)==0)
    {
        printf("INCORRECT MAIL FORMAT\n");
        return 0;
    }
    char *email=getMail(ar);
    char *serverIP=getServer(ar);
    PORT=getPort(ar);
    char *myemail=getMyEmail();
    char subject[50];
    int k,l;
    int in=0;
    for(k=2; k<argc-1; k++)
    {
        int ln=strlen(argv[k]);
        for(l=0; l<ln; l++)
        {
            subject[in]=argv[k][l];
            in++;
        }
        subject[in]=' ';
        in++;
    }
    subject[in]=0;
    char *fname=argv[argc-1];
    char *mailBody=getFile(fname);
    char date[30];
    time_t now = time(NULL);
    struct tm *t = localtime(&now);


    strftime(date, sizeof(date)-1, "DATE %d-%m-%Y %H:%M", t);


    struct sockaddr_in address;
    int sock = 0, valread;
    struct sockaddr_in serv_addr;
    struct hostent *host;
    host = gethostbyname(serverIP);
    sock = socket(AF_INET, SOCK_STREAM, 0);
    memset(&serv_addr, '0', sizeof(serv_addr));

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(PORT);
    serv_addr.sin_addr = *((struct in_addr *)host->h_addr);
    bzero(&(serv_addr.sin_zero),8);
    if(inet_pton(AF_INET, serverIP, &serv_addr.sin_addr)<=0)
    {
        return 0;
    }


    if (connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0)
    {
        printf("\n421 Service not available \r\n");
        return -1;
    }



    char *HELLO="HELO ";
    char *MAIL_FROM="MAIL FROM: <";
    char *MAIL_FROM2=">";
    char *RCPT="RCPT TO: ";
    char *DATA="DATA";
    char *QUIT="QUIT";

    flush();
    valread = read( sock, buffer, 1024);
    if(buffer[0]!='2')
    {
        printf("ERROR %s\n",buffer);
        return 0;
    }
    printf("S: %s\r\n",buffer );
    HELLO=append(HELLO,serverIP);
    send(sock, HELLO, strlen(HELLO), 0 );
    printf("C: %s\r\n",HELLO);

    flush();
    valread = read( sock, buffer, 1024);
    if(buffer[0]!='2')
    {
        printf("ERROR %s\r\n",buffer);
        return 0;
    }
    printf("S: %s\r\n",buffer );

    MAIL_FROM=append(MAIL_FROM,myemail);
    MAIL_FROM=append(MAIL_FROM,MAIL_FROM2);
    send(sock, MAIL_FROM, strlen(MAIL_FROM), 0 );
    printf("C: %s\r\n",MAIL_FROM);

    flush();
    valread = read( sock, buffer, 1024);
    if(buffer[0]!='2')
    {
        printf("ERROR %s\r\n",buffer);
        return 0;
    }
    printf("S: %s\r\n",buffer );

    char *st="<";
    char *en=">";
    RCPT=append(RCPT,st);
    RCPT=append(RCPT,email);
    RCPT=append(RCPT,en);
    send(sock, RCPT, strlen(RCPT), 0 );
    printf("C: %s\r\n",RCPT);

    flush();
    valread = read( sock, buffer, 1024);
    if(buffer[0]!='2')
    {
        printf("ERROR %s\r\n",buffer);
        return 0;
    }
    printf("S: %s\r\n",buffer );


    send(sock, DATA, strlen(DATA), 0 );
    printf("C: %s\r\n",DATA);

    flush();
    valread = read( sock, buffer, 1024);
    if(buffer[0]!='2'&&buffer[0]!='3')
    {
        printf("ERROR %s\r\n",buffer);
        return 0;
    }
    printf("S: %s\r\n",buffer );


    char *from="FROM: <";
    char *from2=">";
    from=append(from,myemail);
    from=append(from,from2);
    send(sock, from, strlen(from), 0 );
    printf("C: %s\r\n",from);

    flush();
    valread = read( sock, buffer, 1024);


    char *to="TO: <";
    char *to2=">";
    to=append(to,email);
    to=append(to,to2);
    send(sock, to, strlen(to), 0 );
    printf("C: %s\r\n",to);

    flush();
    valread = read( sock, buffer, 1024);

    send(sock, date, strlen(date), 0 );
    printf("C: %s\r\n",date);

    flush();
    valread = read( sock, buffer, 1024);

    char *sub="Subject: ";
    sub=append(sub,subject);


    send(sock, sub, strlen(sub), 0 );
    printf("C: %s\r\n",sub);

    flush();
    valread = read( sock, buffer, 1024);




///

    char *spbuff=" ";
    send(sock, spbuff, strlen(spbuff), 0 );
    printf("C: %s\r\n",spbuff);
    flush();
    valread = read( sock, buffer, 1024);

    int maillen=strlen(mailBody);
    char *filebuff=(char *)malloc(maillen*sizeof(char));
    int i,j;
    for(i=0; i<strlen(mailBody); i++)
    {
        filebuff[j]=mailBody[i];
        if(mailBody[i]=='\n'||mailBody[i]==0)
        {
            filebuff[j]=0;
            j=0;
            send(sock, filebuff, strlen(filebuff), 0 );
            printf("C: %s\r\n",filebuff);
            flush();
            valread = read( sock, buffer, 1024);
        }
        else
            j++;
    }
    char *dot=".";
    send(sock, dot, strlen(dot), 0 );
    printf("C: %s\r\n",dot);
///



    flush();
    valread = read( sock, buffer, 1024);
    if(buffer[0]!='2')
    {
        printf("ERROR\r\n");
        return 0;
    }
    printf("S: %s\r\n",buffer );



    send(sock, QUIT, strlen(QUIT), 0 );
    printf("C: %s\r\n",QUIT);

    flush();
    valread = read( sock, buffer, 1024);
    if(buffer[0]!='2')
    {
        printf("S: %s\r\n",buffer );
        printf("ERROR\r\n");
        return 0;
    }
    printf("S: %s\r\n",buffer );

    return 0;
}
