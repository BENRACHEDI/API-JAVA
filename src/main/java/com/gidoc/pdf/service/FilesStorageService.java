package com.gidoc.pdf.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  public void init();
  public void initUpload();
  
  public void save(MultipartFile file);
  
  public Resource load(String filename);

  public Resource loadFile(String filename);
  
  public boolean deleteFile(String filename);
  public boolean deleteModel(String filename);
  public boolean deleteClassement(String filename);

  public void deleteAll();
  
  public Stream<Path> loadListFiles();

  public Stream<Path> loadAllXmlModels();
  public Stream<Path> loadAllXmlData();
  public Stream<Path> loadAllXmlClassements();
}
