package io.codematters.docker;

import java.util.List;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import lombok.SneakyThrows;

public class DockerCommands {

    private final DockerClient client;

    public DockerCommands(DockerClient client) {
        this.client = client;
    }

    // Todo: Have a better standardised way to deal with these common docker exceptions
    @SneakyThrows
    public ContainerCreation createContainer(ContainerConfig containerConfig) {
        return client.createContainer(containerConfig);
    }

    @SneakyThrows
    public List<Container> listContainers() {
        return client.listContainers(DockerClient.ListContainersParam.allContainers());
    }

    @SneakyThrows
    public void startContainer(ContainerCreation container) {
        client.startContainer(container.id());
    }

    @SneakyThrows
    public void stopContainer(ContainerCreation container) {
        final int SECONDS_TO_WAIT_BEFORE_KILLING = 5;
        client.stopContainer(container.id(), SECONDS_TO_WAIT_BEFORE_KILLING);
    }

    @SneakyThrows
    public void removeContainer(ContainerCreation container) {
        client.removeContainer(container.id());
    }

    @SneakyThrows
    public ContainerInfo inspectContainer(ContainerCreation container) {
        return client.inspectContainer(container.id());
    }

    @SneakyThrows
    public void enableContainerLogging(ContainerCreation container) {
        client.attachContainer(container.id(), DockerClient.AttachParameter.values())
                .attach(System.out, System.err, false); //Todo: Maybe this shouldn't be printing to System.out, maybe a logger instead
    }

    public void closeDockerClient() {
        client.close();
    }

}
