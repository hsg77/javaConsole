package com.cwgis;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class test_threads  {

    //定义不返回值的线程功能
    public class ThreadDemo implements Runnable
    {
        private String id="";
        public ThreadDemo(String id)
        {
            this.id=id;
        }
        public void run()
        {
            System.err.println("hello world thread id="+this.id);
        }

    }
    //调用线程的功能
    public static void main(String[] args)
    {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        test_threads threadPoolDemo = new test_threads();
        for (int i = 0; i < 5; i++)
        {
            ThreadDemo threadDemo = threadPoolDemo.new ThreadDemo(Integer.toString(i));  //类中类实例化方法
            Future future = executorService.submit(threadDemo);

            // 可以取消执行
            //future.cancel(true);

            // 可以获取返回结果，如果future.get()!=null 且无异常，表示执行成功
            try
            {
                if (future.get() == null) System.out.println("执行完成");
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
    }
}
