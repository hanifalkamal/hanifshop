package com.hanifshop.productservice.product_service.gridgain;

import org.apache.ignite.IgniteException;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author Hanif al kamal 16/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Component
public class IgniteLifeCycle implements LifecycleBean {

    private final Logger logger =  LogManager.getLogger(IgniteLifeCycle.class);

    @Override
    public void onLifecycleEvent(LifecycleEventType lifecycleEventType) throws IgniteException {
        switch (lifecycleEventType){
            case AFTER_NODE_STOP:
                logger.info("### AFTER_NODE_STOP Called !");
            case AFTER_NODE_START:
                logger.info("### AFTER_NODE_START Called !");
            case BEFORE_NODE_START:
                logger.info("### BEFORE_NODE_START Called !");
            case BEFORE_NODE_STOP:
                logger.info("### BEFORE_NODE_STOP Called !");
        }
    }
}
