package io.codematters.docker;

import java.io.File;
import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.command.BuildImageResultCallback;

public class DockerCmds {
    private final DockerClient dockerClient;

    public DockerCmds(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public CreateContainerResponse createContainer() {
        return dockerClient.createContainerCmd("mongo:3.6")
                .withName("mongo").exec();
    }

    public void startContainer(CreateContainerResponse container) {
        dockerClient.startContainerCmd(container.getId()).exec();
    }

    public InspectContainerResponse inspectContainer(CreateContainerResponse container) {
        return dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public void stopContainer(CreateContainerResponse container) {
        dockerClient.stopContainerCmd(container.getId()).exec();
    }

    public void killContainer(CreateContainerResponse container) {
        dockerClient.killContainerCmd(container.getId()).exec();
    }

    public List<Image> listImages() {
        return dockerClient.listImagesCmd().exec();
    }

    public String buildImage() {
        return dockerClient.buildImageCmd()
                .withDockerfile(new File("docker/Dockerfile"))
                .withPull(true)
                .withNoCache(true)
                .withTag("alpine:git")
                .exec(new BuildImageResultCallback())
                .awaitImageId();
    }

    public InspectImageResponse inspectImage(String imageId) {
        return dockerClient.inspectImageCmd(imageId).exec();
    }

    public void removeImage(String imageId) {
        dockerClient.removeImageCmd(imageId).exec();
    }
}
