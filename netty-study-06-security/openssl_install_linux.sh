# 윈도우 OpenSSL 설정 
# https://blog.naver.com/PostView.nhn?blogId=baekmg1988&logNo=221454486746

# 설치 참고 https://deep-dive-dev.tistory.com/40
# TLS프로토콜을 적용하려면 공개키 암호화에 사용되는 인증서가 필요함
# 인증서는 웹 서버에서 사용하는 인증서와 동일하며 파일은 Pem 형식이어야함
# 인증서는 JDK의 keytool과 같은 인증서 관리도구나 OpenSSL 프로그램을 사용하여 만듬
# keytool을 사용하여 만들어진 인증서는 JKS형식으로만 저장(오라클 개발문서 참고)
# (https://www.lesstif.com/java/java-keytool-keystore-20775436.html_

# 여기서는 OpenSSL을 사용하여 인증서를 생성하는 방법을 설명한다

# open ssl 버전 확인
$ rpm -qa | grep openssl
openssl-libs-1.0.2k-19.el7.x86_64
openssl-1.0.2k-19.el7.x86_64
xmlsec1-openssl-1.2.20-7.el7_4.x86_64

# openssl 버전 업시 한꺼번에 올리면 좋지 않음 - 프로그램 영향도 때문
# 여기서는 1.0.2k -> 1.0.2u 버전으로 업그레이드함
$ wget https://www.openssl.org/source/old/1.0.2/openssl-1.0.2u.tar.gz

# 압축풀기
$ tar zxvf openssl-1.0.2u.tar.gz

# 쉘스크립트 실행
$ cd openssl-1.0.2u/
$ ./config

# make 실행전에 gcc 설치 안되어 있으면 오류발생 gcc설치 필요
$ sudo yum install gcc

# 컴파일
$ make

# 빌드결과 테스트
$ make test

# 빌드된 openssl 설치
$ sudo make install

# 설치 결과 확인
$ openssl version

# 파일 위치 확인
$ which openssl

# 새로 설치한 곳은 /usr/local/ssl/bin/openssl에 존재한다.
# 새로 설치된 파일로 대치하기 위해 심볼릭 링크로 대체하자
$ sudo mv /usr/bin/openssl /usr/bin/openssl.1.0.2k
$ sudo ln -s /usr/local/ssl/bin/openssl /usr/bin/openssl
$ openssl version

# 인증서 생성 
# 인증서 생성전에 개인키와 공개키를 먼저 생성한다.
$ openssl genrsa -aes256 -out privatekey.pem 2048
Enter pass phrase for privatekey.pem: # privatekey.pem 파일의 내용을 보호하기 위한 AES 암호키 입력

# 개인키 생성확인
$ ls -al *.pem
-rw-rw-r-- 1 centos centos 1766  9월 24 11:43 privatekey.pem

# 공개키 생성은 인증서 서명 요청을 위한 CSR을 생성할 때 자동으로 공개키가 생성되어 포함된다.
# CSR파일은 파이을 생성한 기관의 공개카와 우베서비스를 제공하는 회사의 정보를 암호화한 정보가 담겨있다.
# 이파일을 Very Sign과 같은 상위 인증기관으로 전송하여 인증기관의 비밀키로 전자서명을 받으면
# 인증서가 완성된다. CSR 생성하는 코드다
# 실제 상위 인증기관으로 전송하여 전자서명을 받으면 아래 입력값은 수정할 수 없기에 운영서버에 사용할거라면 신중하게
# 입력해야한다.
$ openssl req -new -key privatekey.pem -out netty.csr
Enter pass phrase for privatekey.pem:
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:kr    
State or Province Name (full name) [Some-State]:seoul
Locality Name (eg, city) []:seoul
Organization Name (eg, company) [Internet Widgits Pty Ltd]:foobar
Organizational Unit Name (eg, section) []:foo
Common Name (e.g. server FQDN or YOUR name) []:netty.foo.com
Email Address []:foo@bar.com

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:      
An optional company name []:

# CSR에 전자서몀을 하여 인증서 생성. 
# 작성한 비밀키를 이용하한 전자서명하여 인증서 생성
# 다음은 만료기간이 1년인 인증서 생성
$ openssl x509 \
-in netty.csr -out netty.crt \
-req -signkey privatekey.pem \
-days 356
 
$ ls -al netty*
-rw-rw-r-- 1 centos centos 1273  9월 24 11:55 netty.crt
-rw-rw-r-- 1 centos centos 1041  9월 24 11:52 netty.csr

# 인증서 정합성 검증
$ openssl x509 -noout -text -in ./netty.crt
Certificate:
    Data:
        Version: 1 (0x0)
        Serial Number:
            fe:01:21:3b:7d:40:c8:cf
    Signature Algorithm: sha256WithRSAEncryption # 인증서 전자서명에 사용된 알고리즘
        # 인증서 발급한 기관정보
        Issuer: C=kr, ST=seoul, L=seoul, O=foobar, OU=foo, CN=netty.foor.com/emailAddress=foo@bar.com
        Validity # 인증서 시작일과 만료일
            Not Before: Sep 24 02:55:25 2020 GMT
            Not After : Sep 15 02:55:25 2021 GMT
...

# 개인키 정합성 검증
$ openssl rsa -noout -text -in privatekey.pem
Enter pass phrase for privatekey.pem:
Private-Key: (2048 bit)
...

# 처음 만들었던 개인키 형식은 ASN.1므로 에러발생할 수 있음
# 왜냐하면 SslContextBuilder의 forServer 메서드에는 PKC#8 형식의 개인키가 입력되어야 하기 때문
# Exception in thread "main" java.lang.IllegalArgumentException: File does not contain valid private key: privatekey.pem
# 	at io.netty.handler.ssl.SslContextBuilder.keyManager(SslContextBuilder.java:350)
# 	at io.netty.handler.ssl.SslContextBuilder.forServer(SslContextBuilder.java:107)
# 	at co.kr.hjt.netty.security.HttpSnoopServer.main(HttpSnoopServer.java:41)
# Caused by: java.security.KeyException: could not find a PKCS #8 private key in input stream (see https://netty.io/wiki/sslcontextbuilder-and-private-key.html for more information)
# 	at io.netty.handler.ssl.PemReader.readPrivateKey(PemReader.java:128)
# 	at io.netty.handler.ssl.PemReader.readPrivateKey(PemReader.java:109)
# 	at io.netty.handler.ssl.SslContext.toPrivateKey(SslContext.java:1113)
# 	at io.netty.handler.ssl.SslContextBuilder.keyManager(SslContextBuilder.java:348)
# 	... 2 more
# 다음은 ASN.1을 PCKS#8 형식으로 변환하는 명령이다.
$ cp privatekey.pem privatekey.pem.org
$ openssl pkcs8 -topk8 \
-inform PEM -outform PEM \
-in privatekey.pem.org -out privatekey.pem
