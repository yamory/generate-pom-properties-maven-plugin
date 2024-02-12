package io.yamory;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

@Mojo(
        name = "generatePomProperties",
        requiresDependencyResolution = ResolutionScope.COMPILE
)
public class GeneratePomProperties extends AbstractMojo {

    @org.apache.maven.plugins.annotations.Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(
            property = "project.build.directory",
            required = true
    )
    private File outputDirectory;

    @Parameter(
            property = "outputPomDirectoryName",
            defaultValue = ".pom"
    )
    private String outputPomDirectoryName;

    public void execute() throws MojoExecutionException {
        String outputDirectoryPath = outputDirectory.getPath();
        if (outputDirectoryPath.endsWith("/")) {
            outputDirectoryPath = outputDirectoryPath.substring(0, outputDirectoryPath.length() - 1);
        }

        String pomBaseDir = outputDirectoryPath + "/" + outputPomDirectoryName;
        File pomBaseDirFile = new File(pomBaseDir);
        if (pomBaseDirFile.isDirectory()) {
            deleteFilesRecursively(pomBaseDirFile);
            getLog().debug("Remove output pom.properties directory: " + pomBaseDirFile.getPath());
        }

        getLog().info("Start generate pom.properties: " + pomBaseDir);
        Set<Artifact> artifacts = project.getArtifacts();
        project.getModules();
        for (Artifact artifact : artifacts) {
            String groupId = artifact.getGroupId();
            String artifactId = artifact.getArtifactId();
            String version = artifact.getVersion();

            String pomDirPath = pomBaseDir + "/META-INF/maven/" + groupId + "/" + artifactId;
            File pomDir = new File(pomDirPath);
            pomDir.mkdirs();

            File file = new File(pomDir, "pom.properties");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("groupId=" + groupId + "\n");
                writer.write("artifactId=" + artifactId + "\n");
                writer.write("version=" + version + "\n");
            } catch (IOException e) {
                getLog().error("Failed generate pom.properties: " + file.getPath());
                throw new MojoExecutionException("Error writing pom.properties", e);
            }
            getLog().debug("Generate pom.properties: " + file.getPath());
        }
        getLog().info("End generate pom.properties");
    }

    public boolean deleteFilesRecursively(File rootFile) {
        File[] allFiles = rootFile.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                deleteFilesRecursively(file);
            }
        }
        return rootFile.delete();
    }
}
