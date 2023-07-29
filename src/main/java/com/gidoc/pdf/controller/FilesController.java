package com.gidoc.pdf.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.gidoc.pdf.message.ResponseMessage;
import com.gidoc.pdf.model.AllZoneModel;
import com.gidoc.pdf.model.DataPath;
import com.gidoc.pdf.model.EZone;
import com.gidoc.pdf.model.EZoneList;
import com.gidoc.pdf.model.EZoneListData;
import com.gidoc.pdf.model.FileInfo;
import com.gidoc.pdf.model.ListZoneModel;
import com.gidoc.pdf.model.Textpdf;
import com.gidoc.pdf.model.TextpdfList;
import com.gidoc.pdf.model.ZoneList;
import com.gidoc.pdf.model.Zone;
import com.gidoc.pdf.model.ClassementList;
import com.gidoc.pdf.model.Classement;
import com.gidoc.pdf.service.FilesStorageService;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
/***************************************** */
import java.awt.image.BufferedImage;


@RestController
@RequestMapping("/pdf2")
public class FilesController {

  @Autowired
  FilesStorageService storageService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      storageService.save(file);
      message = "Le fichier a \u00E9t\u00E9 t\u00E9l\u00E9charg\u00E9 avec succ\u00E8s : " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Impossible d'importer le fichier : " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/getFile/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    Resource file = storageService.loadFile(filename);
    //System.out.print(file);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
  
  @GetMapping("/getFiles")
  public ResponseEntity<List<FileInfo>> getListfiles() {
    List<FileInfo> fileInfos = storageService.loadListFiles().map(path -> {
      String filename = path.getFileName().toString();
      String url = Paths.get("RootFiles/uploads/"+path.getFileName().toString()).toAbsolutePath().toString();
      return new FileInfo(filename, url);
    }).collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @DeleteMapping("/deleteFile/{filename:.+}")
  public ResponseEntity<ResponseMessage> deleteFileUpload(@PathVariable String filename) {
    String message = "";
    
    try {
      boolean existed = storageService.deleteFile(filename);   
      if (existed) {
        message = "Delete the file successfully: " + filename;
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      }
      
      message = "The file does not exist!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
    }
  }

  @DeleteMapping("/deleteAllFiles")
  public ResponseEntity<ResponseMessage> deleteAllFilesUpload() {
    String message = "";
    try {
      storageService.deleteAll();   
      message = "Delete all files successfully";
      System.out.println("message delete = "+ message);
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not delete all files . Error: " + e.getMessage();
      System.out.println("message error = "+ message);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/directoryIsEmpty")
  public boolean directoryIsEmpty() {
    String message ;//= "false" ;
    String pathUpload =  Paths.get("RootFiles/uploads/").toString();
    File directory = new File(pathUpload);
    if (!directory.isDirectory()) {
        throw new IllegalArgumentException("The given path is not a directory.");
    }
    if (directory.list().length == 0){
      return false; 
      //message="false";
    }
    else{ 
          return true; 
         // message="true";
        }
    // return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
  }




  /************************************************************ *
   *                      Data Extraire
  /************************************************************ */


  @GetMapping("/xmlData")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    List<FileInfo> fileInfos = storageService.loadAllXmlData().map(path -> {
      String filename = path.getFileName().toString();
      String url = Paths.get("RootFiles/uploads/"+path.getFileName().toString()).toAbsolutePath().toString();
      //Path url = Paths.get("RootFiles/xmlData/"+path.getFileName().toString());
      //String urltest = url.toAbsolutePath().toString();
      //System.out.println(url);
      //String ur+*l = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

      return new FileInfo(filename, url);
    }).collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/xmlData/{filename:.+}")
  public ResponseEntity<Resource> getFileXml(@PathVariable String filename) {
    Resource file = storageService.load(filename);
    //System.out.print(file);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @DeleteMapping("/xmlData/{filename:.+}")
  public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
    String message = "";
    
    try {
      boolean existed = storageService.deleteFile(filename);
      
      if (existed) {
        message = "Delete the file successfully: " + filename;
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      }
      
      message = "The file does not exist!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
    }
  }



  /**************************************************************** *
   *                  Classement
  /**************************************************************** */

  @GetMapping("/xmlClassements")
  public ResponseEntity<List<FileInfo>> getListXmlClassements() {
    List<FileInfo> classementInfos = storageService.loadAllXmlClassements().map(path -> {
      String filename = path.getFileName().toString();
      String url = Paths.get("RootFiles/xmlClassements/"+path.getFileName().toString()).toString();
      return new FileInfo(filename, url);
    }).collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(classementInfos);
  }
  
/*   @GetMapping("/xmlClassements/{filename:.+}")
  public ResponseEntity<List<FileInfo>> getXmlClassements(@PathVariable String filename) {
    List<FileInfo> classementInfos = storageService.loadXmlClassements().map(path -> {
      String filename = path.getFileName().toString();
      String url = Paths.get("RootFiles/xmlClassements/"+path.getFileName().toString()).toString();
      return new FileInfo(filename, url);
    }).collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(classementInfos);
  } */
  
  @RequestMapping("/addClassementXml")
  public ResponseEntity<String> addClassementXml(@RequestBody ClassementList xmlClassementData) {  
      String nomClassementlXml = xmlClassementData.getNomClassement();
      String nomModelXml = xmlClassementData.getNomModel();
      Path pathModel = Paths.get("RootFiles/xmlClassements/"+nomClassementlXml+".xml");
      JAXBContext jaxbContext;
      String responseMessage;
      try {
          jaxbContext = JAXBContext.newInstance(ClassementList.class);
          Marshaller marshaller = jaxbContext.createMarshaller();
          marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
          marshaller.marshal(xmlClassementData, System.out);
          marshaller.marshal(xmlClassementData, new File(pathModel.toString())); 
          responseMessage = "Le Classement a été bien créé";
        return new ResponseEntity<String>(responseMessage, HttpStatus.OK);
      } catch (JAXBException e) {
          e.printStackTrace();
          responseMessage = "Erreur lors de la création du classement : " + e.getMessage();
        return new ResponseEntity<String>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
      } 
}
    
  @DeleteMapping("/deleteClassement/{filename:.+}")
  public ResponseEntity<ResponseMessage> deleteClassement(@PathVariable String filename) {
    String message = "";
    try {
      boolean existed = storageService.deleteClassement(filename);   
      if (existed) {
        message = "Delete the classement successfully: " + filename;
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      }
      
      message = "The classement does not exist!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not delete the classement: " + filename + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
    }
  }  



  /**************************************************************** *
   *                    Modèle
  /**************************************************************** */

  @GetMapping("/xmlModels")
  public ResponseEntity<List<FileInfo>> getListXmlModels() {
    List<FileInfo> fileInfos = storageService.loadAllXmlModels().map(path -> {
      String filename = path.getFileName().toString();
      String url = Paths.get("RootFiles/xmlModels/"+path.getFileName().toString()).toString();
      //String url = urlpath.toString();
      //String url = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

      return new FileInfo(filename, url);
    }).collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @RequestMapping("/addModelXml")
  public ResponseEntity<String> addModelXml(@RequestBody ZoneList xmlModelData) {  
      String nomModelXml = xmlModelData.getNomModel();
      Path pathModel = Paths.get("RootFiles/xmlModels/"+nomModelXml+".xml");
      JAXBContext jaxbContext;
      String responseMessage;
      try {
          jaxbContext = JAXBContext.newInstance(ZoneList.class);
          Marshaller marshaller = jaxbContext.createMarshaller();
          marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
          //marshaller.marshal(xmlModelData, System.out);
          marshaller.marshal(xmlModelData, new File(pathModel.toString())); 
          responseMessage = "Le Modèle a été bien créé";
        return new ResponseEntity<String>(responseMessage, HttpStatus.OK);
      } catch (JAXBException e) {
          e.printStackTrace();
          responseMessage = "Erreur lors de la création du modèle : " + e.getMessage();
        return new ResponseEntity<String>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
      } 
 }
    
  @DeleteMapping("/deleteModel/{filename:.+}")
  public ResponseEntity<ResponseMessage> deleteModel(@PathVariable String filename) {
    String message = "";
    
    try {
      boolean existed = storageService.deleteModel(filename);   
      if (existed) {
        message = "Delete the model successfully: " + filename;
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      }
      
      message = "The model does not exist!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not delete the model: " + filename + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
    }
  }  

  @RequestMapping("/NomZoneModel")
  public ResponseEntity<ArrayList<ListZoneModel>> getNomZoneModel(@RequestBody String nomModel) throws JAXBException {
     String pathModel = Paths.get("RootFiles/xmlModels/"+nomModel).toString();

    JAXBContext jaxbContext = JAXBContext.newInstance(ZoneList.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    ZoneList zoneList = (ZoneList) unmarshaller.unmarshal(new File(pathModel));

                ArrayList<ListZoneModel> listZoneModel = new ArrayList<>();
                int id=0;
                for(Zone zone: zoneList.zoneList){ id++;
                        listZoneModel.add(new ListZoneModel(id ,zone.getnomZone()));
                }      
    return ResponseEntity.status(HttpStatus.OK).body(listZoneModel);
  }





  /*************************************************************** *
     *               Annotation
  /*************************************************************** */

  @RequestMapping("/AllZoneModel")
  public ResponseEntity<ZoneList> getAllZoneModel(@RequestBody String nomModel) throws JAXBException {
     String pathModel = Paths.get("RootFiles/xmlModels/"+nomModel).toString();

    JAXBContext jaxbContext = JAXBContext.newInstance(ZoneList.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    ZoneList zoneList = (ZoneList) unmarshaller.unmarshal(new File(pathModel));    
    return ResponseEntity.status(HttpStatus.OK).body(zoneList);
  }

  @RequestMapping("/SingleExtraction")
  public ResponseEntity<EZoneList> SingleExtraction(@RequestBody EZoneList ezoneList) throws JAXBException, IOException, TesseractException {  
     
     String imgCropped = Paths.get("RootFiles/imgTemp/").toString().replace("\\", "/")+"/";
     String tessdataPath = Paths.get("src/main/resources/tessdata/").toString().replace("\\", "/")+"/"; 
     String pathNomPDF= Paths.get("RootFiles/uploads/"+ezoneList.getNomFile()).toString();

     for (EZone ezone : ezoneList.getEZoneList()) {
        PDDocument doc = PDDocument.load(new File(pathNomPDF));  
          //Cropped image and save it.
          int p = ezone.getp();
          int x = ezone.getx(); float yy = ezone.gety();
          int w = ezone.getw(); int h = ezone.geth();  
          PDPage page = doc.getPage(p);
          float height =  page.getCropBox().getHeight(); 
          float y = (height - yy) - h;
          page.setCropBox(new PDRectangle(x, y, w, h));
          PDFRenderer renderer = new PDFRenderer(doc);
          BufferedImage img = renderer.renderImage(p, 4f);
          ImageIOUtil.writeImage(img, new File(imgCropped, "imgCropped.jpg").getAbsolutePath(), 100);
          
          //Get extract text from image save it.
          ITesseract tesseract = new Tesseract();
          tesseract.setTessVariable("user_defined_dpi", "72");
          tesseract.setDatapath(tessdataPath); 
          String extractedText =  tesseract.doOCR(new File(imgCropped, "imgCropped.jpg"));
          ezone.settextZone(extractedText);
          doc.close();    
        }      
       
  return ResponseEntity.status(HttpStatus.OK).body(ezoneList);                    
}

   @RequestMapping("/AllExtraction")
   public static ResponseEntity<List<EZoneList>> AllExtraction(@RequestBody String nomModel) throws JAXBException, IOException, TesseractException {  
    String pathModele= Paths.get("RootFiles/xmlModels/"+nomModel).toString();
    String pathUpload= Paths.get("RootFiles/uploads/").toString().replace("\\", "/")+"/";
   
    String imgCropped = Paths.get("RootFiles/imgTemp/").toString().replace("\\", "/")+"/";
    String tessdataPath = Paths.get("src/main/resources/tessdata/").toString().replace("\\", "/")+"/"; 

    JAXBContext jaxbContext = JAXBContext.newInstance(ZoneList.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    ZoneList zoneList = (ZoneList) unmarshaller.unmarshal(new File(pathModele));

    List<EZoneList> ezoneList = new ArrayList<>();
/*    File directory = new File(pathUpload);
     if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                  if (file.isFile() && file.getName().endsWith(".pdf")) {
                      String fileName = file.getName();
                      String path_pdf = pathUpload + fileName;
                      //System.out.println(path_pdf);
                      List<EZone> ezone = new ArrayList<>();
                      for(Zone zone: zoneList.zoneList){
                         PDDocument doc = PDDocument.load(new File(path_pdf));  
                        //Cropped image and save it.
                        String z = zone.getnomZone(); int p = zone.getp();
                        int x = zone.getx(); int yy = zone.gety();
                        int w = zone.getw(); int h = zone.geth();  
                        PDPage page = doc.getPage(p);
                        float height =  page.getCropBox().getHeight(); 
                        float y = (height - yy) - h;
                        page.setCropBox(new PDRectangle(x, y, w, h));
                        PDFRenderer renderer = new PDFRenderer(doc);
                        BufferedImage img = renderer.renderImage(p, 4f);
                        ImageIOUtil.writeImage(img, new File(imgCropped, "imgCropped.jpg").getAbsolutePath(), 100);
                        
                        //Get extract text from image save it.
                        ITesseract tesseract = new Tesseract();
                        tesseract.setTessVariable("user_defined_dpi", "72");
                        tesseract.setDatapath(tessdataPath); 
                        String extractedText =  tesseract.doOCR(new File(imgCropped, "imgCropped.jpg"));
                        ezone.add(new EZone(z, extractedText, p, x, yy, w, h));
                        doc.close();  
                      }   
                      ezoneList.add(new EZoneList(ezone, nomModel, fileName));
                    }
                }
            } 
        } else {
            System.out.println("Invalid directory path");
        }
   */     
 return ResponseEntity.status(HttpStatus.OK).body(ezoneList);    
}

   @RequestMapping("/SaveListExtraction")
   public ResponseEntity<ResponseMessage> SaveListExtraction(@RequestBody EZoneListData ClassementEZoneList) throws JAXBException, IOException {  
    List<EZoneList> ListezoneList = ClassementEZoneList.getEZoneListArray();
    String pathClassement= Paths.get("RootFiles/xmlClassements/"+ClassementEZoneList.getNomClassement()).toString();

    JAXBContext jaxbContext = JAXBContext.newInstance(ClassementList.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    ClassementList classementList = (ClassementList) unmarshaller.unmarshal(new File(pathClassement));
    String regle="";
    for(Classement classement: classementList.getClassementList()){
      regle+="/"+classement.getnomZone();
    } 
    
    JAXBContext jaxbContext2;
    String responseMessage="responseMessage null";
    jaxbContext2 = JAXBContext.newInstance(EZoneList.class);
    Marshaller marshaller = jaxbContext2.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    
    Path pathRegle =  Paths.get("RootFiles/xmlData"+ regle+"/");
    if(!Files.exists(pathRegle)){
      Files.createDirectories(pathRegle);
    }

    try {   
        for (EZoneList ezoneList : ListezoneList) {
          Path pathFile =  Paths.get(pathRegle +"/"+  ezoneList.getNomFile().replace(".pdf", ".xml"));
          marshaller.marshal(ezoneList, new File(pathFile.toString())); 
          responseMessage = "La création de xml data à été bien créé";
        }
          deleteAllFilesUpload();
          return  ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(responseMessage)); 
      } catch (JAXBException e) {
          e.printStackTrace();
          responseMessage = "Erreur lors de la création du x data : " + e.getMessage();
          return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(responseMessage)); 
      }    

 //return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(responseMessage));    
}



/*    @RequestMapping("/extractionXml")
   public static String extractionXml(@RequestBody DataPath dataPath) throws JAXBException, IOException, TesseractException {  
    String pathModele = dataPath.getPathModel().replace("\\", "/")+"/";
    String pathPdf = dataPath.getPathPdf().replace("\\", "/")+"/";
  
    System.out.println(pathModele);
    System.out.println(pathPdf);
    String imgCropped = Paths.get("RootFiles/imgTemp/").toString().replace("\\", "/")+"/";
    String tessdataPath = Paths.get("src/main/resources/tessdata/").toString().replace("\\", "/")+"/"; 
    String xmlDataOutput = Paths.get("RootFiles/xmlData/").toString().replace("\\", "/")+"/";

    JAXBContext jaxbContext = JAXBContext.newInstance(ZoneList.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    ZoneList zoneList = (ZoneList) unmarshaller.unmarshal(new File(pathModele));

    File directory = new File(pathPdf);
     if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".pdf")) {
                        String fileName = file.getName();
                        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
                        String path_pdf = pathPdf+file.getName();

                TextpdfList textpdfList = new TextpdfList();
                for(Zone zone: zoneList.zoneList){
                        PDDocument doc = PDDocument.load(new File(path_pdf));  
                        //Cropped image and save it.
                        String z = zone.getnomZone(); int p = zone.getp();
                        int x = zone.getx(); int yy = zone.gety();
                        int w = zone.getw(); int h = zone.geth();  
                        PDPage page = doc.getPage(p);
                        float height =  page.getCropBox().getHeight(); 
                        float y = (height - yy) - h;
                         page.setCropBox(new PDRectangle(x, y, w, h));
                        PDFRenderer renderer = new PDFRenderer(doc);
                        BufferedImage img = renderer.renderImage(p, 4f);
                        ImageIOUtil.writeImage(img, new File(imgCropped, "imgCropped.jpg").getAbsolutePath(), 100);
                        
                        //Get extract text from image save it.
                        ITesseract tesseract = new Tesseract();
                        tesseract.setTessVariable("user_defined_dpi", "72");
                        tesseract.setDatapath(tessdataPath); 
                        String extractedText =  tesseract.doOCR(new File(imgCropped, "imgCropped.jpg"));
                        textpdfList.textpdf.add(new Textpdf(z, extractedText));             
                        doc.close();    
                }      
               System.out.println(" xmlDataOutput = "+xmlDataOutput);
               String filePathOut = xmlDataOutput+fileNameWithoutExtension+".xml";   
               System.out.println(" filePathOut = "+filePathOut);
                JAXBContext jaxbContext2 = JAXBContext.newInstance(TextpdfList.class);
                Marshaller marshaller2 = jaxbContext2.createMarshaller();
                marshaller2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller2.marshal(textpdfList, new File(filePathOut));  
                      }
                }
            } 
        } else {
            System.out.println("Invalid directory path");
        }

 return  "Terminer";
}
 */


/*  private static final String UPLOAD_DIR = "C:/Users/SIDO/Desktop/myUpload/";
    @PostMapping("/uploadfiles")
    public List<String> uploadFiles(@RequestBody List<MultipartFile> files) {
        List<String> uploadedFiles = new ArrayList<>();

        System.out.println("*************************************");
        System.out.println(files);
        System.out.println("*************************************");

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String filePath = UPLOAD_DIR + fileName;

            try {
                // Save the file to the specified directory
                Path targetPath = Path.of(filePath);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                uploadedFiles.add(fileName);
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace();
            }
        }

        return uploadedFiles;
    }
 
 */



}
