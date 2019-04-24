package com.sflpro.notifier.services.notification.impl.email.mandrill;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.externalclients.email.mandrill.model.request.SendEmailRequest;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 4/16/19
 * Time: 8:55 PM
 */
public class EmailNotificationMandrillProviderProcessorImplTest extends AbstractServicesUnitTest {

    @TestSubject
    private EmailNotificationMandrillProviderProcessorImpl emailNotificationMandrillProviderProcessor = new EmailNotificationMandrillProviderProcessorImpl();

    @Mock
    private MandrillApiCommunicator mandrillApiCommunicator;

    @Test
    public void testProcessEmailNotificationWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            emailNotificationMandrillProviderProcessor.processEmailNotification(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
            emailNotification.setProviderType(NotificationProviderType.SMTP_SERVER);
            emailNotificationMandrillProviderProcessor.processEmailNotification(emailNotification);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
            emailNotification.setProviderType(NotificationProviderType.MANDRILL);
            emailNotification.setTemplateName(null);
            emailNotificationMandrillProviderProcessor.processEmailNotification(emailNotification);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessEmailNotification() {
        // Test data
        final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
        emailNotification.setProviderType(NotificationProviderType.MANDRILL);
        // Reset
        resetAll();
        // Expectations
        expect(mandrillApiCommunicator.sendEmailTemplate(isA(SendEmailRequest.class))).andAnswer(() -> {
            final SendEmailRequest sendEmailRequest = (SendEmailRequest) getCurrentArguments()[0];
            assertSendEmailRequest(sendEmailRequest, emailNotification);
            return true;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = emailNotificationMandrillProviderProcessor.processEmailNotification(emailNotification);
        assertTrue(result);
        // Verify
        verifyAll();
    }

    private void assertSendEmailRequest(final SendEmailRequest request, final EmailNotification notification) {
        assertEquals(notification.getRecipientEmail(), request.getRecipientMail());
        assertEquals(notification.getTemplateName(), request.getTemplateName());
        notification.getProperties().forEach(emailNotificationProperty -> {
            assertTrue(request.getTemplateContent().containsKey(emailNotificationProperty.getPropertyKey()));
            assertEquals(request.getTemplateContent().get(emailNotificationProperty.getPropertyKey()), emailNotificationProperty.getPropertyValue());
        });
    }
}