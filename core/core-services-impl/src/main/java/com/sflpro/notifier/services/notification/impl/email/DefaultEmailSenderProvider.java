package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.email.SimpleEmailSender;
import com.sflpro.notifier.email.SimpleEmailSenderRegistry;
import com.sflpro.notifier.email.TemplatedEmailSender;
import com.sflpro.notifier.email.TemplatedEmailSenderRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:57 AM
 */

class DefaultEmailSenderProvider implements EmailSenderProvider {

    private final Map<String, SimpleEmailSender> registeredSimpleEmailSenders;
    private final Map<String, TemplatedEmailSender> registeredTemplatedEmailSenders;

    DefaultEmailSenderProvider(final List<SimpleEmailSenderRegistry> simpleEmailSenderRegistries,
                               final List<TemplatedEmailSenderRegistry> templatedEmailSenderRegistries) {
        this.registeredSimpleEmailSenders = simpleEmailSenderRegistries.stream()
                .collect(Collectors.toMap(SimpleEmailSenderRegistry::name, SimpleEmailSenderRegistry::sender));
        this.registeredTemplatedEmailSenders = templatedEmailSenderRegistries.stream()
                .collect(Collectors.toMap(TemplatedEmailSenderRegistry::name, TemplatedEmailSenderRegistry::sender));
    }

    @Override
    public Optional<SimpleEmailSender> lookupSimpleEmailSenderFor(final String providerType) {
        return Optional.ofNullable(registeredSimpleEmailSenders.get(providerType));
    }

    @Override
    public Optional<TemplatedEmailSender> lookupTemplatedEmailSenderFor(final String providerType) {
        return Optional.ofNullable(registeredTemplatedEmailSenders.get(providerType));
    }
}
