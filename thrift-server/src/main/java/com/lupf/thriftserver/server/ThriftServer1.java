package com.lupf.thriftserver.server;

import com.lupf.thriftapi.StudentService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ThriftServer1 {
    //端口
    @Value("${server.thrift.port}")
    private Integer port;

    //最小线程数
    @Value("${server.thrift.min-thread-pool}")
    private Integer minThreadPool;

    //最大线程数
    @Value("${server.thrift.max-thread-pool}")
    private Integer maxThreadPool;

    //真实的用于处理业务的service
    @Autowired
    MyServerServiceImpl myServerService;


    public void start() {
        try {

            // 非阻塞式的，配合TFramedTransport使用
            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);

            // 关联处理器与Service服务的实现
            TProcessor processor = new StudentService.Processor<StudentService.Iface>(myServerService);

            // 目前Thrift提供的最高级的模式，可并发处理客户端请求
            TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(serverTransport);

            args.processor(processor);

            // 设置协议工厂，高效率的、密集的二进制编码格式进行数据传输协议
            args.protocolFactory(new TCompactProtocol.Factory());

            // 设置传输工厂，使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO
            // 这里的传输工厂要和客户端使用的是同一种，否则将无法正常使用
            args.transportFactory(new TFramedTransport.Factory());

            // 设置处理器工厂,只返回一个单例实例
            args.processorFactory(new TProcessorFactory(processor));

            // 多个线程，主要负责客户端的IO处理
            // 默认的方式也是使用的newFixedThreadPool线程池的方式
            // 可以通过指定下面的参数来设置相关线程池的大小
            args.selectorThreads(2);
            args.workerThreads(maxThreadPool);

            // 工作线程池
            // 如果这里使用executorService传入线程池的话，
            // 那么new TThreadedSelectorServer的时候就不会创建线程池，而是传入的下面自己定义的线程池
            //ExecutorService pool = Executors.newFixedThreadPool(100);
            //args.executorService(pool);

            TThreadedSelectorServer server = new TThreadedSelectorServer(args);

            System.out.println("shrift server started; port:" + port);

            server.serve();

        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
