package com.santosfc.sitesantosfc.model;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // Faz upload e retorna a URL pública da imagem
    public String uploadImagem(MultipartFile file) throws IOException {
        Map resultado = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap("folder", "santosfc")
        );
        return (String) resultado.get("secure_url");
    }

    // Deleta imagem pelo URL público
    public void deletarImagem(String urlImagem) {
        try {
            // Extrai o public_id da URL
            // Ex: https://res.cloudinary.com/cloud/image/upload/v123/santosfc/nome.jpg
            String[] partes = urlImagem.split("/");
            String nomeComExtensao = partes[partes.length - 1];
            String publicId = "santosfc/" + nomeComExtensao.split("\\.")[0];
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
