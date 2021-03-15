package io.codematters.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import lombok.SneakyThrows;

import static java.lang.Integer.parseInt;

public class DockerConfig {

    private final String image;
    private final DockerClient client;
    private final ContainerConfig containerConfig;
    private final String PORT = "3999";

    public DockerConfig(String image) {
        this.image = image;
        this.client = setupClient();

        pullImage();
        this.containerConfig = setupContainerConfig();
    }

    // Todo: Have a better standardised way to deal with these common docker exceptions
    @SneakyThrows
    private DockerClient setupClient() {
        return DefaultDockerClient.fromEnv().build();
    }

    @SneakyThrows
    private void pullImage() {
        client.pull(image);
    }

    private ContainerConfig setupContainerConfig() {
        HostConfig hostConfig = HostConfig
                .builder()
                .portBindings(
                        ImmutableMap.of(
                                PORT,
                                ImmutableList.of(
                                        PortBinding.of("0.0.0.0", parseInt(PORT))
                                )
                        )
                ).build();

        return ContainerConfig
                .builder()
                .image(image)
                .env(
                        "MYSQL_ROOT_PASSWORD=p$ssw0rd",
                        "MYSQL_DATABASE=my_app_db"
                )
                .exposedPorts(PORT)
                .hostConfig(hostConfig)
                .build();
    }

    public String getImage() {
        return image;
    }

    public DockerClient getClient() {
        return client;
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }
}
