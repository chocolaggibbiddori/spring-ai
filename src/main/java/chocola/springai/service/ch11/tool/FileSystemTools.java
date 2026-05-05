package chocola.springai.service.ch11.tool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class FileSystemTools {

    private final Path rootDirectory;

    public FileSystemTools() {
        Path home = Paths.get(System.getProperty("user.home"));
        rootDirectory = home.resolve("Desktop/study/self_study/spring-ai/filesystem");

        try {
            Files.createDirectories(rootDirectory);
        } catch (IOException e) {
            log.error("Failed to create root directory: {}", rootDirectory, e);
            throw new RuntimeException("Failed to initialize FileSystemTools", e);
        }
    }

    private Path resolve(String relativePath) {
        if (!StringUtils.hasText(relativePath)) {
            return rootDirectory;
        }

        Path path = rootDirectory.resolve(relativePath).normalize();
        return path.startsWith(rootDirectory) ? path : rootDirectory;
    }

    @Tool(description = "디렉토리 항목 조회")
    public List<Item> listFiles(String relativePath) {
        Path path = resolve(relativePath);

        try (Stream<Path> listStream = Files.list(path)) {
            return listStream
                    .map(p -> new Item(p, Files.isDirectory(p)))
                    .toList();
        } catch (IOException e) {
            log.info(e.toString());
            return new ArrayList<>();
        }
    }

    @Tool(description = "디렉토리 생성")
    public String createDir(String relativePath) {
        Path path = resolve(relativePath);

        try {
            Files.createDirectories(path);
            return "디렉토리 생성 성공";
        } catch (FileAlreadyExistsException e) {
            log.error("Directory already exists: {}", path, e);
            return "디렉토리가 이미 존재합니다.";
        } catch (IOException e) {
            log.error("Failed to create directory: {}", path, e);
            return "디렉토리 생성 실패";
        }
    }

    @Tool(description = "파일 생성")
    public String createFile(@ToolParam(description = "부모 디렉토리") String parentPath,
                             @ToolParam(description = "파일 이름") String filename,
                             @ToolParam(description = "확장 이름") String extension,
                             @ToolParam(description = "파일 내용") String content) {
        if (!StringUtils.hasText(filename) || !StringUtils.hasText(extension)) {
            return "디렉토리 또는 파일명이 없습니다.";
        }

        if (!StringUtils.hasText(content)) {
            content = "";
        }

        Path path = resolve(parentPath);
        path = !filename.endsWith("." + extension)
                ? path.resolve(filename + "." + extension)
                : path.resolve(filename);

        try {
            Files.writeString(path, content, StandardCharsets.UTF_8);
            return "파일 생성 성공";
        } catch (IOException e) {
            log.error("Failed to create file: {}", path, e);
            return "파일 생성 실패";
        }
    }

    @Tool(description = "파일 및 디렉토리 삭제")
    public String deletePath(String relativePath) {
        Path path = resolve(relativePath);

        if (path.equals(rootDirectory)) {
            return "루트 디렉토리는 삭제할 수 없습니다.";
        }

        if (Files.notExists(path)) {
            return "파일 또는 디렉토리가 존재하지 않습니다.";
        }

        try {
            if (Files.isDirectory(path)) {
                Files.walkFileTree(path, new SimpleFileVisitor<>() {

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        if (exc != null) {
                            throw exc;
                        }

                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                Files.delete(path);
            }

            return "삭제 성공";
        } catch (IOException e) {
            log.error(e.toString());
            return "파일 또는 디렉토리를 삭제하지 못했습니다.";
        }
    }

    @Tool(description = "파일 이동 또는 이름 변경")
    public String moveFile(String sourceRelativePath, String targetRelativePath) {
        Path sourcePath = resolve(sourceRelativePath);
        Path targetPath = resolve(targetRelativePath);

        if (sourcePath.equals(targetPath)) {
            return "원본과 대상이 동일합니다. 파일 이동을 건너뜁니다.";
        }

        if (sourcePath.equals(rootDirectory)) {
            return "루트 디렉토리의 이동은 허용되지 않습니다.";
        }

        try {
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "파일 이동 성공";
        } catch (IOException e) {
            log.error("Failed to move file: {} to {}", sourcePath, targetPath, e);
            return "파일 이동에 실패했습니다.";
        }
    }

    public record Item(Path path, boolean directory) {
    }
}
