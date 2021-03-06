package com.sflpro.notifier.services.notification;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/24/15
 * Time: 1:00 PM
 */
public interface NotificationProcessor {

    /**
     * Process notification
     *
     * @param notificationId
     */
    void processNotification(@Nonnull final Long notificationId, @Nonnull final Map<String, String> secureProperties);
}
