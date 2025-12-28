package net.ritasister.dc.util.utility.updater;

import net.ritasister.dc.DonateCasePaperPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public final class UpdateDownloaderGitHub {

    private final DonateCasePaperPlugin donateCasePaperPlugin;

    private static final String LATEST_RELEASE_URL = "https://api.github.com/repos/{owner}/{repo}/releases/latest";
    private static final String JAR_DOWNLOAD_URL_PATTERN = "https://github.com/{owner}/{repo}/releases/download/{tag}/{fileName}";
    private boolean updateDownloaded;

    public UpdateDownloaderGitHub(DonateCasePaperPlugin donateCasePaperPlugin) {
        this.donateCasePaperPlugin = donateCasePaperPlugin;
    }

    public void downloadLatestJar() {
        try {
            final String organization = "RSTeamCore";
            final String pluginName = donateCasePaperPlugin.getBootstrap().getLoader().getName();
            final String currentVersion = donateCasePaperPlugin.getBootstrap().getLoader().getDescription().getVersion();
            final String latestReleaseTag = getLatestReleaseTag(pluginName);

            final String fileNew = String.format("DonateCase-%s.jar", latestReleaseTag);
            final String fileCurrent = String.format("DonateCase-%s.jar", currentVersion);
            final String newVersionPluginPath = donateCasePaperPlugin.getBootstrap().getLoader().getDataFolder().getParentFile() + File.separator + fileNew;
            final String oldVersionPluginPath = donateCasePaperPlugin.getBootstrap().getLoader().getDataFolder().getParentFile() + File.separator + fileCurrent;
            final File newJar = new File(newVersionPluginPath);
            final File oldJar = new File(oldVersionPluginPath);

            if (updateDownloaded) {
                donateCasePaperPlugin.getLogger().info("Update is already marked as downloaded. Skipping download.");
                return;
            }

            if (newJar.exists()) {
                donateCasePaperPlugin.getLogger().info("The latest update is already downloaded. Skipping download.");
                updateDownloaded = true;
                removeOldJar(fileNew, oldJar);
            } else {
                if (latestReleaseTag != null) {
                    final String jarDownloadUrl = JAR_DOWNLOAD_URL_PATTERN
                            .replace("{owner}", organization)
                            .replace("{repo}", pluginName)
                            .replace("{tag}", latestReleaseTag)
                            .replace("{fileName}", fileNew);
                    donateCasePaperPlugin.getLogger().info("Starting download from URL: " + jarDownloadUrl);
                    downloadFile(jarDownloadUrl, newVersionPluginPath);
                    donateCasePaperPlugin.getLogger().info("Download process completed successfully.");
                    removeOldJar(fileNew, oldJar);
                    updateDownloaded = true;
                } else {
                    donateCasePaperPlugin.getLogger().warn("Failed to get the latest release tag. Aborting download.");
                }
            }
        } catch (Exception exception) {
            donateCasePaperPlugin.getLogger().severe("Error while downloading the latest jar: " + exception.getMessage());
        }
    }

    private void removeOldJar(String fileNew, File fileCurrent) {
        try {
            final File directory = new File(donateCasePaperPlugin.getBootstrap().getLoader().getDataFolder().getParentFile().getPath());
            final File[] files = directory.listFiles((dir, name) -> name.startsWith("WorldGuardRegionProtect-") && name.endsWith(".jar"));

            if (files != null) {
                for (File file : files) {
                    if (!file.getName().equals(fileNew) && !file.getName().equals(fileCurrent.getName())) {
                        donateCasePaperPlugin.getLogger().info("Removing old jar file: " + file.getName());
                        Files.delete(file.toPath());
                    }
                }
            }
        } catch (IOException exception) {
            donateCasePaperPlugin.getLogger().severe("Failed to remove old jar files: " + exception.getMessage());
        }
    }

    private String getLatestReleaseTag(String repo) throws IOException {
        final String url = LATEST_RELEASE_URL
                .replace("{owner}", "RSTeamCore")
                .replace("{repo}", repo);
        final StringBuilder response = new StringBuilder();
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setRequestMethod("GET");

            try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
                final byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.append(new String(buffer, 0, bytesRead));
                }
            } catch (IOException exception) {
                donateCasePaperPlugin.getLogger().warn("Failed to get release info from api.github.com." + exception.getMessage());
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return parseJsonForTag(response.toString());
    }

    private @Nullable String parseJsonForTag(@NotNull String json) {
        final int tagNameIndex = json.indexOf("\"tag_name\"");
        if (tagNameIndex != -1) {
            final int start = json.indexOf("\"", tagNameIndex + 11) + 1;
            final int end = json.indexOf("\"", start);
            return json.substring(start, end);
        }
        return null;
    }

    private void downloadFile(String fileUrl, String savePath) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(fileUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {

            final byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            donateCasePaperPlugin.getLogger().info("File downloaded successfully. Saved to: " + savePath);
        } catch (IOException e) {
            donateCasePaperPlugin.getLogger().severe("Error occurred during file download: " + e.getMessage());
            throw e;
        } finally {
            connection.disconnect();
        }
    }

}
