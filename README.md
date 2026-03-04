# Hello World

This application opens a server socket on port 35810 and listens for connections.
Once connected the socket for the communication is wrapped in a ConnectionHandler.
The ConnectionHandler is executed in a new virtual thread (see ThreadPerTaskExecutor::execute).

After a client has connected and sent at least 30 bytes two threads start eating up CPU cycles.
The status of the threads constantly switch between THDW and RUN.

Call stack of the BOTH threads:

```
Art   Programm                 Anweisung         Prozedur                     
      QLESPI     QSYS          20                LE_Create_Thread2__FP12crtt >
      QP0WPINT   QSYS          19                pthread_create_part2         
      QP2USER2   QSYS          3                 __Qp2Thread                  
      QP2USER2   QSYS          4                 runpase_common__FiPvT2       
 P    libpthreads.a(shr_xpg >          000000FC  _pthread_body                
 P    libj9thr29.so            1758    0000055C  thread_wrapper               
 P    libj9vm29.so             414     0000006C  javaThreadProc               
 P    libj9prt29.so            425     000004BC  omrsig_protect               
 P    libj9vm29.so             2213    000000F4  javaProtectedThreadProc__FP >
 J    jdk/internal/misc/Inn >          00000014  run                          
 J    java/lang/Thread                 00000013  run                          
 J    java/lang/Thread                 00000005  runWith                      
 J    sun/nio/ch/Poller$$La >          00000004  run                          
 J    sun/nio/ch/Poller                00000001  pollLoop                     
 J    sun/nio/ch/Poller                00000002  poll                         
 J    sun/nio/ch/PollsetPol >          00000028  poll                         
 J    sun/nio/ch/PollsetPol >          0000000C  pollInner                    
 J    sun/nio/ch/Pollset                         pollsetPoll                  
 P    libj9vm29.so             682     00000248  runJavaThread                
 P    libj9jit29.so                    00000278  jitAcquireVMAccess           
 P    libj9jit29.so                    0000004C  fast_jitAcquireVMAccess      
 P    libj9vm29.so             409     00000114  internalEnterVMFromJNI§AF41 >
 P    libj9vm29.so             ...     00000044  internalAcquireVMAccessNoMu >
 P    libj9vm29.so             388     000001C4  internalAcquireVMAccessNoMu >
 P    libj9thr29.so            4619    00000050  omrthread_monitor_wait       
 P    libj9thr29.so            4777    000009A4  IPRA.$monitor_wait_original  
 P    libpthreads.a(shr_xpg >          000001AC  pthread_cond_wait      
 P    libpthreads.a(shr_xpg >          00000038  _cond_wait             
 P    libpthreads.a(shr_xpg >          00000518  _cond_wait_local       
 P    libpthreads.a(shr_xpg >          00000354  _event_wait            
 P    libpthreads.a(shr_xpg >          00000514  _event_sleep           
 P    unix                             00000008  <syscall>:thread_tsleep
```

A thread dump shows that the threads "Read-Poller" and "Write-Poller" might be the culprits because the show the same call stack.

```
"Read-Poller" prio=5 Id=46 RUNNABLE
        at java.base@21.0.9/sun.nio.ch.Pollset.pollsetPoll(Native Method)
        at java.base@21.0.9/sun.nio.ch.PollsetPoller.pollInner(PollsetPoller.java:99)
        at java.base@21.0.9/sun.nio.ch.PollsetPoller.poll(PollsetPoller.java:88)
        at java.base@21.0.9/sun.nio.ch.Poller.poll(Poller.java:370)
        at java.base@21.0.9/sun.nio.ch.Poller.pollLoop(Poller.java:277)
        at java.base@21.0.9/java.lang.Thread.run(Thread.java:1595)
        at java.base@21.0.9/jdk.internal.misc.InnocuousThread.run(InnocuousThread.java:186)
```

```
"Write-Poller" prio=5 Id=48 RUNNABLE
        at java.base@21.0.9/sun.nio.ch.Pollset.pollsetPoll(Native Method)
        at java.base@21.0.9/sun.nio.ch.PollsetPoller.pollInner(PollsetPoller.java:99)
        at java.base@21.0.9/sun.nio.ch.PollsetPoller.poll(PollsetPoller.java:88)
        at java.base@21.0.9/sun.nio.ch.Poller.poll(Poller.java:370)
        at java.base@21.0.9/sun.nio.ch.Poller.pollLoop(Poller.java:277)
        at java.base@21.0.9/java.lang.Thread.run(Thread.java:1595)
        at java.base@21.0.9/jdk.internal.misc.InnocuousThread.run(InnocuousThread.java:186)
```


## Requirements

Java 21


## Build

```
mvn clean package
```

## Start

```
/QOpenSys/QIBM/ProdData/JavaVM/jdk21/64bit/bin/java -cp helloworld.jar  helloworld.socket.Main
```

## Testing

The simplest way to test this is to use a HTTP client.

cURL:
```
curl http://localhost:35810
```

SQL:
```
VALUES QSYS2.HTTP_GET('http://localhost:35810');
```

Web browser:

Just enter the URL: http://my_IBM_i:35810


Note: The interesting part is that a request originating from the same machine does not trigger the 
      misbehaving of the threads. Only a request from outside the machine does.
      
      
      
## Thread Dump

```
JRE 21 OS/400 ppc64-64-Bit Compressed References 20251211_000000 (JIT enabled, AOT enabled)
OpenJ9   - 14b3b2de26
OMR      - d4c7e3040
JCL      - e60e40b6af5 based on jdk-21.0.9+10

"main" prio=5 Id=2 RUNNABLE
        at java.base@21.0.9/sun.nio.ch.Net.accept(Native Method)
        at java.base@21.0.9/sun.nio.ch.NioSocketImpl.accept(NioSocketImpl.java:748)
        at java.base@21.0.9/java.net.ServerSocket.implAccept(ServerSocket.java:698)
        at java.base@21.0.9/java.net.ServerSocket.platformImplAccept(ServerSocket.java:663)
        at java.base@21.0.9/java.net.ServerSocket.implAccept(ServerSocket.java:639)
        at java.base@21.0.9/java.net.ServerSocket.implAccept(ServerSocket.java:585)
        at java.base@21.0.9/java.net.ServerSocket.accept(ServerSocket.java:543)
        at app//helloworld.socket.Server.start(Server.java:15)
        at app//helloworld.socket.Main.main(Main.java:6)

"JIT Compilation Thread-000" prio=10 Id=4 RUNNABLE

"JIT Compilation Thread-001 Suspended" prio=10 Id=5 RUNNABLE

"JIT Compilation Thread-002 Suspended" prio=10 Id=6 RUNNABLE

"JIT Compilation Thread-003 Suspended" prio=10 Id=7 RUNNABLE

"JIT Compilation Thread-004 Suspended" prio=10 Id=8 RUNNABLE

"JIT Compilation Thread-005 Suspended" prio=10 Id=9 RUNNABLE

"JIT Compilation Thread-006 Suspended" prio=10 Id=10 RUNNABLE

"JIT Diagnostic Compilation Thread-007 Suspended" prio=10 Id=11 RUNNABLE

"JIT-SamplerThread" prio=10 Id=12 TIMED_WAITING

"IProfiler" prio=5 Id=13 RUNNABLE

"Common-Cleaner" prio=8 Id=3 TIMED_WAITING
        at java.base@21.0.9/java.lang.Object.waitImpl(Native Method)
        at java.base@21.0.9/java.lang.Object.wait(Object.java:255)
        at java.base@21.0.9/java.lang.Object.wait(Object.java:221)
        at java.base@21.0.9/java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:138)
        at java.base@21.0.9/jdk.internal.ref.CleanerImpl.run(CleanerImpl.java:140)
        at java.base@21.0.9/java.lang.Thread.run(Thread.java:1595)
        at java.base@21.0.9/jdk.internal.misc.InnocuousThread.run(InnocuousThread.java:186)

"Finalizer thread" prio=5 Id=14 RUNNABLE

"Concurrent Mark Helper" prio=1 Id=15 RUNNABLE

"GC Worker" prio=5 Id=16 RUNNABLE

"GC Worker" prio=5 Id=17 RUNNABLE

"GC Worker" prio=5 Id=18 RUNNABLE

"GC Worker" prio=5 Id=19 RUNNABLE

"GC Worker" prio=5 Id=20 RUNNABLE

"GC Worker" prio=5 Id=21 RUNNABLE

"GC Worker" prio=5 Id=22 RUNNABLE

"i5/OS information agent" prio=5 Id=23 RUNNABLE

"Attach API wait loop" prio=10 Id=26 RUNNABLE
        at java.base@21.0.9/openj9.internal.tools.attach.target.IPC.waitSemaphore(Native Method)
        at java.base@21.0.9/openj9.internal.tools.attach.target.CommonDirectory.waitSemaphore(CommonDirectory.java:264)
        at java.base@21.0.9/openj9.internal.tools.attach.target.WaitLoop.waitForNotification(WaitLoop.java:66)
        at java.base@21.0.9/openj9.internal.tools.attach.target.WaitLoop.run(WaitLoop.java:157)

"Read-Poller" prio=5 Id=46 RUNNABLE
        at java.base@21.0.9/sun.nio.ch.Pollset.pollsetPoll(Native Method)
        at java.base@21.0.9/sun.nio.ch.PollsetPoller.pollInner(PollsetPoller.java:99)
        at java.base@21.0.9/sun.nio.ch.PollsetPoller.poll(PollsetPoller.java:88)
        at java.base@21.0.9/sun.nio.ch.Poller.poll(Poller.java:370)
        at java.base@21.0.9/sun.nio.ch.Poller.pollLoop(Poller.java:277)
        at java.base@21.0.9/java.lang.Thread.run(Thread.java:1595)
        at java.base@21.0.9/jdk.internal.misc.InnocuousThread.run(InnocuousThread.java:186)

"Read-Updater" prio=5 Id=47 WAITING
        at java.base@21.0.9/jdk.internal.misc.Unsafe.park(Native Method)
        at java.base@21.0.9/java.util.concurrent.locks.LockSupport.park(LockSupport.java:371)
        at java.base@21.0.9/java.util.concurrent.LinkedTransferQueue$DualNode.await(LinkedTransferQueue.java:458)
        at java.base@21.0.9/java.util.concurrent.LinkedTransferQueue.xfer(LinkedTransferQueue.java:613)
        at java.base@21.0.9/java.util.concurrent.LinkedTransferQueue.take(LinkedTransferQueue.java:1257)
        at java.base@21.0.9/sun.nio.ch.Poller.updateLoop(Poller.java:293)
        at java.base@21.0.9/java.lang.Thread.run(Thread.java:1595)
        at java.base@21.0.9/jdk.internal.misc.InnocuousThread.run(InnocuousThread.java:186)

"Write-Poller" prio=5 Id=48 RUNNABLE
        at java.base@21.0.9/sun.nio.ch.Pollset.pollsetPoll(Native Method)
        at java.base@21.0.9/sun.nio.ch.PollsetPoller.pollInner(PollsetPoller.java:99)
        at java.base@21.0.9/sun.nio.ch.PollsetPoller.poll(PollsetPoller.java:88)
        at java.base@21.0.9/sun.nio.ch.Poller.poll(Poller.java:370)
        at java.base@21.0.9/sun.nio.ch.Poller.pollLoop(Poller.java:277)
        at java.base@21.0.9/java.lang.Thread.run(Thread.java:1595)
        at java.base@21.0.9/jdk.internal.misc.InnocuousThread.run(InnocuousThread.java:186)

"Write-Updater" prio=5 Id=49 WAITING
        at java.base@21.0.9/jdk.internal.misc.Unsafe.park(Native Method)
        at java.base@21.0.9/java.util.concurrent.locks.LockSupport.park(LockSupport.java:371)
        at java.base@21.0.9/java.util.concurrent.LinkedTransferQueue$DualNode.await(LinkedTransferQueue.java:458)
        at java.base@21.0.9/java.util.concurrent.LinkedTransferQueue.xfer(LinkedTransferQueue.java:613)
        at java.base@21.0.9/java.util.concurrent.LinkedTransferQueue.take(LinkedTransferQueue.java:1257)
        at java.base@21.0.9/sun.nio.ch.Poller.updateLoop(Poller.java:293)
        at java.base@21.0.9/java.lang.Thread.run(Thread.java:1595)
        at java.base@21.0.9/jdk.internal.misc.InnocuousThread.run(InnocuousThread.java:186)

"Attachment portNumber: 42276" prio=10 Id=50 RUNNABLE
        at java.base@21.0.9/openj9.internal.tools.attach.target.DiagnosticUtils.dumpAllThreadsImpl(Native Method)
        at java.base@21.0.9/openj9.internal.tools.attach.target.DiagnosticUtils.getThreadInfo(DiagnosticUtils.java:322)
        at java.base@21.0.9/openj9.internal.tools.attach.target.DiagnosticUtils.executeDiagnosticCommand(DiagnosticUtils.java:258)
        at java.base@21.0.9/openj9.internal.tools.attach.target.Attachment.doCommand(Attachment.java:249)
        at java.base@21.0.9/openj9.internal.tools.attach.target.Attachment.run(Attachment.java:160)

"file lock watchdog" prio=10 Id=51 TIMED_WAITING
        at java.base@21.0.9/java.lang.Object.waitImpl(Native Method)
        at java.base@21.0.9/java.lang.Object.wait(Object.java:255)
        at java.base@21.0.9/java.lang.Object.wait(Object.java:221)
        at java.base@21.0.9/java.util.TimerThread.mainLoop(Timer.java:570)
        at java.base@21.0.9/java.util.TimerThread.run(Timer.java:523)
```