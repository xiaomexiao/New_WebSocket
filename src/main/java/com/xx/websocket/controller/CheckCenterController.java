package com.xx.websocket.controller;

import com.xx.websocket.util.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Random;

@Controller
@RequestMapping("/checkcenter")
public class CheckCenterController {

    /**
     * 当前有那个用户加入了web socket
     */
    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav = new ModelAndView("/socket");
        mav.addObject("cid", cid);
        return mav;
    }

    /**
     * 推送数据的接口
     */
//推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public String pushToWeb(@PathVariable String cid, String message) {

        /**
         * 推送数据信息放到一个线程中去
         */
        Thread t1 = new Thread(() -> {
            int bitPrice = 100000;
            while (true) {

                //每隔1-3秒就产生一个新价格
                int duration = 1000 + new Random().nextInt(2000);
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //新价格围绕100000左右50%波动
                float random = 1 + (float) (Math.random() - 0.5);
                int newPrice = (int) (bitPrice * random);

                //查看的人越多，价格越高
                int total = WebSocketServer.getTotal();
                newPrice = newPrice * total;

                String messageFormat = "{\"price\":\"%d\",\"total\":%d}";
                String message1 = String.format(messageFormat, newPrice, total);

                try {
                    WebSocketServer.sendInfo(message1, cid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        return cid;
    }
}