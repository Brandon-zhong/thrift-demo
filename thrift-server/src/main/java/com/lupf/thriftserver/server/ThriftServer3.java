package com.lupf.thriftserver.server;

import com.lupf.thriftapi.StudentService;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ThriftServer3 {
    //监听的端口
    @Value("${server.thrift.port}")
    private Integer port;

    //线程池最小线程数
    @Value("${server.thrift.min-thread-pool}")
    private Integer minThreadPool;

    //线程池最大线程数
    @Value("${server.thrift.max-thread-pool}")
    private Integer maxThreadPool;

    //业务服务对象
    @Autowired
    MyServerServiceImpl myServerService;

    public void start() {
        try {
            TMultiplexedProcessor processor = new TMultiplexedProcessor();

            processor.registerProcessor("StudentService", new StudentService.Processor<>(myServerService));
//            processor.registerProcessor("MultiplyService",
//                    new MultiplyService.Processor(new MultiplyHandler()));

            TServerTransport serverTransport = new TServerSocket(port);

            TServer.Args args = new TServer.Args(serverTransport);
            args.processor(processor);

            TTransportFactory factory = new TFramedTransport.Factory();
            args.transportFactory(factory);
            args.protocolFactory(new TBinaryProtocol.Factory());

            TSimpleServer server = new TSimpleServer(args);

            System.out.println("Starting server on port 7911 ...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
