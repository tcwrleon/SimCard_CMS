package com.sunyard.util;
/********************************************
 * 文件名称: SMSUtil <br/>
 * 系统名称: F2F
 * 模块名称: WEB业务平台
 * 软件版权: 信雅达系统工程股份有限公司
 * 功能说明: TODO ADD FUNCTION. <br/>
 * 系统版本: 1.0.0.1
 * 开发人员:  Terrance
 * 开发时间: 2015/8/12 16:17
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *********************************************/

import com.sunyard.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.*;
import org.smslib.modem.SerialModemGateway;

import java.io.IOException;
import java.util.Properties;

public class SMSUtil {

    private static Service smsService = null;

    private static Logger logger = LoggerFactory.getLogger(SMSUtil.class);

    private SMSUtil() {
    }

    private static synchronized Service getInstance() {
        if (smsService == null) {
            smsService = Service.getInstance();
            Properties properties = PropertiesUtil.newInstance("SMSSender.properties");
            String modem = properties.getProperty("modem");
            String com = properties.getProperty("com");
            SerialModemGateway gateway = new SerialModemGateway(modem, com, 115200, "wavecom", "17254");//115200是波特率，一般为9600。可以通过超级终端测试出来
            gateway.setInbound(false);
            gateway.setOutbound(true);
            gateway.setSimPin("0000");
            try {
                smsService.addGateway(gateway);
            } catch (GatewayException e) {
                logger.info("请检查com口信息是否正确");
                e.printStackTrace();
            }
            try {
                smsService.startService();
            } catch (SMSLibException e) {
                logger.info("短信服务启动失败！");
                e.printStackTrace();
            } catch (IOException e) {
                logger.info("短信服务启动失败！");
                e.printStackTrace();
            } catch (InterruptedException e) {
                logger.info("短信服务启动失败！");
                e.printStackTrace();
            }

        }
        return smsService;
    }

/*
    static {
        Properties properties = PropertiesUtil.newInstance("SMSSender.properties");
        String modem = properties.getProperty("modem");
        String com = properties.getProperty("com");
        SerialModemGateway gateway = new SerialModemGateway(modem, com, 115200, "wavecom", "17254");//115200是波特率，一般为9600。可以通过超级终端测试出来
        gateway.setInbound(false);
        gateway.setOutbound(true);
        gateway.setSimPin("0000");
        try {
            smsService.addGateway(gateway);
        } catch (GatewayException e) {
            logger.info("请检查com口信息是否正确");
            e.printStackTrace();
        }
        try {
            smsService.startService();
        } catch (SMSLibException e) {
            logger.info("短信服务启动失败！");
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("短信服务启动失败！");
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.info("短信服务启动失败！");
            e.printStackTrace();
        }
    }
*/

    public static void sendSms(String phoneNo, String SMSContent) {
        OutboundMessage msg = new OutboundMessage(phoneNo, SMSContent + " 本条信息请勿回复");
        msg.setEncoding(Message.MessageEncodings.ENCUCS2);//发中文短信
        try {
            getInstance().sendMessage(msg);
        } catch (TimeoutException e) {
            logger.info("信息发送超时");
            e.printStackTrace();
        } catch (GatewayException e) {
            logger.info("com口设置错误");
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("短信服务系统异常！");
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.info("短信服务系统异常！");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SMSUtil.sendSms("15268522126", "你好，你的验证码是 abc，30A分钟后失效");
        //SMSUtil.sendSms("15268522126", "你好，你的验证码是 def，30A分钟后失效");

       /* for (int i = 0; i <10 ; i++) {
            final int m = i ;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SMSUtil.sendSms("15268522126","你好，你的验证码是"+ m +"，30A分钟后失效");
                }
            }).start();
        }*/

    }

    public void stopService() {
        try {
            smsService.stopService();
        } catch (SMSLibException e) {
            logger.info("短信服务关闭异常！");
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("短信服务关闭异常！");
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.info("短信服务关闭异常！");
            e.printStackTrace();
        }
    }

    public class OutboundNotification implements IOutboundMessageNotification {
        public void process(AGateway gateway, OutboundMessage msg) {
            logger.info("Gateway===============: " + gateway.getGatewayId());
            logger.info("Gateway===============: " + msg);
        }
    }
}
