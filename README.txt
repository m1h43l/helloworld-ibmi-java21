Build
=====

mvn clean package


Test branch datareader
======================

Windows using OpenJDK
---------------------

java -version output:

openjdk version "21.0.5" 2024-10-15 LTS
OpenJDK Runtime Environment Temurin-21.0.5+11 (build 21.0.5+11-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.5+11 (build 21.0.5+11-LTS, mixed mode, sharing)

Executing the test:

java -cp ".\target\helloworld.jar;.\target\libs\*" helloworld.DataReaderTest

Output:

HttpPrologue[protocol=HTTP, protocolVersion=1.1, method=GET, uriPath=/, query=, fragment=]


IBM i 7.5 with Java 21
----------------------

java -version output:

java version "21.0.3" 2024-04-16
IBM Semeru Runtime Certified Edition (build 21.0.3+9)
Eclipse OpenJ9 VM (build openj9-0.44.0, JRE 21 OS/400 ppc64-64-Bit Compressed References 20250430_000000 (JIT enabled, AOT enabled)
OpenJ9   - b0699311c7
OMR      - 254af5a04
JCL      - 27911390f76 based on jdk-21.0.3+9)

Executing the test:

/QOpenSys/QIBM/ProdData/JavaVM/jdk21/64bit/bin/java -cp ./helloworld.jar:libs/* helloworld.DataReaderTest

Output:

Exception in thread "main" io.helidon.http.RequestException: Invalid prologue: Found CR (0) without following LF. :
+--------+-------------------------------------------------+----------------+
|   index|  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |            data|
+--------+-------------------------------------------------+----------------+
|00000000| 47 45 54 20 2f 20 48 54 54 50 2f 31 2e 31 0d 0a |GET / HTTP/1.1..|
|00000010| 48 6f 73 74 3a 20 31 32 37 2e 30 2e 30 2e 31 3a |Host: 127.0.0.1:|
|00000020| 38 31 38 31 0d 0a 55 73 65 72 2d 41 67 65 6e 74 |8181..User-Agent|
|00000030| 3a 20 63 75 72 6c 2f 38 2e 36 2e 30 0d 0a 41 63 |: curl/8.6.0..Ac|
|00000040| 63 65 70 74 3a 20 2a 2f 2a 0d 0a 0d 0a          |cept: */*....   |
+--------+-------------------------------------------------+----------------+

        at io.helidon.http.RequestException$Builder.build(RequestException.java:157)
        at io.helidon.webserver.http1.Http1Prologue.doRead(Http1Prologue.java:171)
        at io.helidon.webserver.http1.Http1Prologue.readPrologue(Http1Prologue.java:103)
        at helloworld.DataReaderTest.main(DataReaderTest.java:17)
Caused by: io.helidon.common.buffers.DataReader$IncorrectNewLineException: Found CR (0) without following LF. :
+--------+-------------------------------------------------+----------------+
|   index|  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |            data|
+--------+-------------------------------------------------+----------------+
|00000000| 47 45 54 20 2f 20 48 54 54 50 2f 31 2e 31 0d 0a |GET / HTTP/1.1..|
|00000010| 48 6f 73 74 3a 20 31 32 37 2e 30 2e 30 2e 31 3a |Host: 127.0.0.1:|
|00000020| 38 31 38 31 0d 0a 55 73 65 72 2d 41 67 65 6e 74 |8181..User-Agent|
|00000030| 3a 20 63 75 72 6c 2f 38 2e 36 2e 30 0d 0a 41 63 |: curl/8.6.0..Ac|
|00000040| 63 65 70 74 3a 20 2a 2f 2a 0d 0a 0d 0a          |cept: */*....   |
+--------+-------------------------------------------------+----------------+

        at io.helidon.common.buffers.DataReader.findNewLine(DataReader.java:435)
        at io.helidon.webserver.http1.Http1Prologue.doRead(Http1Prologue.java:165)
        ... 2 more


Note: I used the helloworld.jar compiled on the Windows PC for both tests (on Windows and on IBM i).


