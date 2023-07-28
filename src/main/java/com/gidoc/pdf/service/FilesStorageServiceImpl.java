package com.gidoc.pdf.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  private final Path rootUploads = Paths.get("RootFiles/uploads");
  private final Path rootXmlModels = Paths.get("RootFiles/xmlModels");
  private final Path rootXmlData = Paths.get("RootFiles/xmlData");
  private final Path rootXmlClassements = Paths.get("RootFiles/xmlClassements");

  @Override
  public void init() {
    try {
      Files.createDirectories(rootUploads);
      Files.createDirectories(rootXmlModels);
      Files.createDirectories(rootXmlData);
      Files.createDirectories(rootXmlClassements);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for Data!");
    }
  }

  @Override
  public void initUpload() {
    try {
      Files.createDirectories(rootUploads);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for Data!");
    }
  }

  @Override
  public void save(MultipartFile file) {
    try {
      Files.copy(file.getInputStream(), this.rootUploads.resolve(file.getOriginalFilename()));
    } catch (Exception e) {
      if (e instanceof FileAlreadyExistsException) {
        throw new RuntimeException("A file of that name already exists.");
      }

      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public Resource load(String filename) {
    try {
      Path file = rootXmlModels.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public Resource loadFile(String filename) {
    try {
      Path file = rootUploads.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public boolean deleteFile(String filename) {
    try {
      Path file = rootUploads.resolve(filename);
      return Files.deleteIfExists(file);
    } catch (IOException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }
  @Override
  public boolean deleteModel(String filename) {
    try {
      Path file = rootXmlModels.resolve(filename);
      return Files.deleteIfExists(file);
    } catch (IOException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }
  @Override
  public boolean deleteClassement(String filename) {
    try {
      Path file = rootXmlClassements.resolve(filename);
      return Files.deleteIfExists(file);
    } catch (IOException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootUploads.toFile());
    initUpload();
  }

  @Override
  public Stream<Path> loadAllXmlModels() {
    try {
      return Files.walk(this.rootXmlModels, 1).filter(path -> !path.equals(this.rootXmlModels)).map(this.rootXmlModels::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the models!");
    }
  }
  @Override
  public Stream<Path> loadAllXmlData() {
    try {
      return Files.walk(this.rootXmlData, 1).filter(path -> !path.equals(this.rootXmlData)).map(this.rootXmlData::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the data!");
    }
  }
  @Override
  public Stream<Path> loadAllXmlClassements() {
    try {
      return Files.walk(this.rootXmlClassements, 1).filter(path -> !path.equals(this.rootXmlClassements)).map(this.rootXmlClassements::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the classement!");
    }
  }
  @Override
  public Stream<Path> loadListFiles() {
    try {
      return Files.walk(this.rootUploads, 1).filter(path -> !path.equals(this.rootUploads)).map(this.rootUploads::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }

}
