package com.example.demo;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.entity.exception.BizException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import java.io.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import static cn.hutool.core.io.IoUtil.DEFAULT_BUFFER_SIZE;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaApplicationTests {


    private final PathMatcher pathMarch = new AntPathMatcher();


    ExecutorService executor = Executors.newCachedThreadPool();














    @Test
    public void CovertTest() {
       // Object[] a = {"a", "你", "好", "", 1};
        Object[] a = null ;
        // 将数组转集合 (需要 判断 空)
        //List<?> list01 = Arrays.asList(a);

        // 不需要 判断 null ,如果是 null 返回的 是 null
        List<?> list0 = Convert.convert(List.class, a);
        //从4.1.11开始可以这么用 （内部有非空判断）
        List<?> list1 = Convert.toList(a);


        // 字符串 转  LocalDateTime
        LocalDateTime localDateTime1 = Convert.toLocalDateTime("2020-08-14");
        LocalDate localDate = localDateTime1.toLocalDate();

        System.out.println(localDateTime1);
        System.out.println(localDate);



    }


    @Test
    public void MoneyTest() {
     // 金额大小写转换

        //结果为："陆万柒仟伍佰伍拾陆元叁角贰分"
        String digitUppercase1 = Convert.digitToChinese(BigDecimal.valueOf(67556.00d));
        String digitUppercase2 = Convert.digitToChinese(BigDecimal.valueOf(67556.01d));
        System.out.println(digitUppercase1);
        System.out.println(digitUppercase2);

    }



    @Test
    public void IOTest() throws IOException {

        FileUtil.newFile("d:/test.txt");
        FileUtil.newFile("d:/test2.txt");
        // 拷贝(u盘 ，磁盘 etc)
        // 将 test.txt 的内容 拷贝到  test2
        BufferedInputStream in = FileUtil.getInputStream("d:/test.txt");
        BufferedOutputStream out = FileUtil.getOutputStream("d:/test2.txt");
        // 最底层是个 while 循环 ，读一部分，写入到 另一个地方，此方法也不需要 进行关闭流的操作，因为已经封装了
        // 底层代码:
        //  while((readSize = in.read(buffer)) != -1) {
        //                out.write(buffer, 0, readSize);
        //                size += (long)readSize;
        //                out.flush();
        //                if (null != streamProgress) {
        //                    streamProgress.progress(size);
        //                }
        //            }
        long copySize = IoUtil.copy(in, out, DEFAULT_BUFFER_SIZE);


        // copy(InputStream in, OutputStream out)


        // 用工具包
        // BufferedOutputStream out2 = FileUtil.getOutputStream("d:/test2.txt");
        // 自己 new 一个输出流对象
         BufferedOutputStream out2 =  new BufferedOutputStream(new FileOutputStream(new  File("d:/test2.txt")));
         IoUtil.copy(new ByteArrayInputStream("hhhhhhhhhhhhhhhhhh".getBytes()),out2,DEFAULT_BUFFER_SIZE);


        // 可以正常写入
         BufferedOutputStream out3 =  new BufferedOutputStream(new FileOutputStream(new  File("d:/test2.txt")));
         IoUtil.copy(new ByteArrayInputStream("您好a!".getBytes()),out3,DEFAULT_BUFFER_SIZE);


        // 汉字
        OutputStreamWriter out4 =  new OutputStreamWriter(new FileOutputStream(new  File("d:/test2.txt")),CharsetUtil.CHARSET_UTF_8);
        long copy = IoUtil.copy(new InputStreamReader(new ByteArrayInputStream("您好!".getBytes(CharsetUtil.CHARSET_UTF_8))), out4, DEFAULT_BUFFER_SIZE);



        // method
        // byte[] readBytes(InputStream in)
        // 说明：读取一个 流()

        // 不使用工具，自己写
        // ① 获取输入流
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("您好a!".getBytes());

        // ② 创建缓存字节数组，用来存放 从流读取的内容 （假设这个区域很大）
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        // ③ 读取流 并将内容 放到 buffer 字节数组缓存里
        int read = byteArrayInputStream.read(buffer);

        String result = new String(buffer);
        System.out.println(result);
        System.out.println(result);
        // 上述方法的缺点： 如果读取的内容过大 会有数据丢失情况

        // 使用工具
        ByteArrayInputStream byteArrayInputStream3 = new ByteArrayInputStream("您好a!".getBytes());
//        底层 实现：
//        byte[] buffer = new byte[bufferSize];
//        if (null != streamProgress) {
//            streamProgress.start();
//        }
//
//        long size = 0L;
//
//        int readSize;
//        try {
//            while((readSize = in.read(buffer)) != -1) {
//                out.write(buffer, 0, readSize);
//                size += (long)readSize;
//                out.flush();
//                if (null != streamProgress) {
//                    streamProgress.progress(size);
//                }
//            }
//        } catch (IOException var8) {
//            throw new IORuntimeException(var8);
//        }


        // 然后  out.toByteArray()
        byte[] bytes = IoUtil.readBytes(byteArrayInputStream3);
        String result2 = new String(bytes);
        System.out.println(result2);


    }

    @Test
    public void StringTest() throws IOException{

        String template = "{}爱{}，就像老鼠爱大米";
        String str = StrUtil.format(template, "我", "你"); //str -> 我爱你，就像老鼠爱大米
        System.out.println(str);


    }



    @Test
    public void Test38() throws IOException{

        String  PICTURE_CRM_URI = "crm3" ;
        String  endPoint = "crm333" ;
        String  OLD_DATA = "old" ;

        String str = "old";

//        String posUrl = Arrays.stream(str.split("|")).filter(ele -> ele.contains("IMG"))
//                .collect(Collectors.joining("|"));


        String posUrl = Arrays.stream(str.split("\\|")).filter(ele -> (!ele.contains(PICTURE_CRM_URI)  && !ele.contains(endPoint) &&  !ele.contains(OLD_DATA) ))
                .collect(Collectors.joining("|"));

        System.out.println(posUrl);



    }




    @Test
    public void supplyAsyncTest() throws IOException, ExecutionException, InterruptedException {
        System.out.println("main  thread ......"+Thread.currentThread().getName());
        // 带返回值的
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("run start ...");
            System.out.println("task  thread ......"+Thread.currentThread().getName());
            System.out.println("run end ...");
            // 返回执行结果
            return 1;
        });
//        Integer result = future.get();
//        System.out.println("result = "+result);

    }





    /**
    * @Description:  计算结果完成时的回调方法
    * @Param:
    * @return:
    * @Author: guoyiguang
    * @Date:
    */
    @Test
    public void whenCompleteTest() throws IOException, ExecutionException, InterruptedException {
        System.out.println("main  thread ......"+Thread.currentThread().getName());
        String  token = "token123456789" ;
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("run start ...");
            Long i = 0L;
            while(i<500L){
                System.out.println("task  thread ......"+Thread.currentThread().getName());
            }
            System.out.println(token);
            System.out.println("run end ...");
            // 返回执行结果
            i++;
            return 1;
        });
        //  主线程陷入阻塞
        Integer result = future.get();
        System.out.println("result = "+result);



        // whenCompleteAsync
        future.whenComplete(new BiConsumer<Integer, Throwable>() {
            @Override
            public void accept(Integer t, Throwable action) {
                System.out.println("执行完成！ thread name  为  "+Thread.currentThread().getName());
            }

        });
        future.exceptionally(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable t) {
                System.out.println("执行失败！"+t.getMessage());
                return null;
            }
        });

    }



    /**
     * @Description:  计算结果完成时的回调方法
     * @Param:
     * @return:
     * @Author: guoyiguang
     * @Date:
     */
    @Test
    public void whenCompleteTest1() throws IOException, ExecutionException, InterruptedException {
        System.out.println("main  thread ......"+Thread.currentThread().getName());
        String  token = "token123456789" ;
        CompletableFuture<Integer> future =   CompletableFuture.supplyAsync(() -> {

            System.out.println("run start ...");
            Long i = 0L;
            while(i<500L){
                // int i1 = 1 / 0;
                throw new RuntimeException("hhhhhh");
            }
            System.out.println(token);
            System.out.println("run end ...");

            // 返回执行结果
            i++;
            return 1;
       }).exceptionally(ex->{
            String message = ex.getMessage();
            System.out.println(message);
            return 0 ;

        });




//        future.exceptionally(new Function<Throwable, Integer>() {
//            @Override
//            public Integer apply(Throwable t) {
//                System.out.println("exception "+Thread.currentThread().getName());
//
//                System.out.println("执行失败！"+t.getMessage());
//
//
//                return null;
//            }
//        });

        Integer integer = future.get();
        System.out.println(integer);
        boolean done = future.isDone();

        System.out.println(done);

        boolean completedExceptionally = future.isCompletedExceptionally();
        System.out.println(completedExceptionally);







    }



    /**
     * @Description:  计算结果完成时的回调方法
     * @Param:
     * @return:
     * @Author: guoyiguang
     * @Date:
     */
    @Test
    public void whenCompleteTest2() throws IOException, ExecutionException, InterruptedException {
        System.out.println("main  thread ......"+Thread.currentThread().getName());
        String  token = "token123456789" ;
        CompletableFuture<Integer> future =   CompletableFuture.supplyAsync(() -> {
            System.out.println("run start ...");
            Long i = 0L;
            while(i<500L){
                throw BizException.wrap("hhhhhh");
            }
            System.out.println(token);
            System.out.println("run end ...");

            // 返回执行结果
            i++;
            return 1;
        });






        try {
            Integer integer = future.get();
            // 获取异常信息
        }catch (ExecutionException  e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());;
            System.out.println(e.toString());;
        } catch (BizException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }










    }


    @Test
    public void whenCompleteTest3() throws IOException, ExecutionException, InterruptedException, IllegalAccessException, InstantiationException {
        // executor
        System.out.println("main  thread ......"+Thread.currentThread().getName());
        String  token = "token123456789" ;

        Future<Integer> future = executor.submit(new Callable<Integer>() {
            public Integer call() {
                // 以异步方式在新的线程中执行耗时的操作
                Long i = null;
                try {
                    System.out.println("run start ...");
                    i = 0L;
                    while(i<500L){
                        throw BizException.wrap("hhhhhh");
                    }
                    System.out.println(token);
                    System.out.println("run end ...");
                } catch (BizException e) {

                   return 0 ;
                }

                // 返回执行结果
                i++;
                return 1;
            }
        });


        try {
            // 获取异步操作的结果，如果最终被阻塞，无法得到结果，那么在最多等待1秒钟之后退出
            Integer result = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());

        } catch (TimeoutException e) {
        }



    }










}
