package com.agile.gateway.register;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
public class SwaggerDocRegister extends Subscriber<InstancesChangeEvent> {

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    private final DiscoveryClient discoveryClient;

    /**
     * Event callback method, handling {@link InstancesChangeEvent} events.
     *
     * @param event Event object
     */
    @Override
    public void onEvent(InstancesChangeEvent event) {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrlSet = discoveryClient.getServices()
                .stream()
                // Get service instance
                .flatMap(serviceId -> discoveryClient.getInstances(serviceId).stream())
                // Filter out instances that contain the "spring-doc" key in the metadata
                .filter(instance -> StringUtils.isNotBlank(instance.getMetadata().get("spring-doc")))
                .map(instance -> {
                    AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
                    swaggerUrl.setName(instance.getServiceId());
                    swaggerUrl.setUrl(String.format("/%s/v3/api-docs", instance.getMetadata().get("spring-doc")));
                    return swaggerUrl;
                })
                .collect(Collectors.toSet());

        // The set of collected SwaggerUrl set into the swaggerUiConfigProperties
        swaggerUiConfigProperties.setUrls(swaggerUrlSet);
    }

    /**
     * Subscription type method, which returns the subscribed event type.
     *
     * @return Type of event to subscribe to
     */
    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }

}
    