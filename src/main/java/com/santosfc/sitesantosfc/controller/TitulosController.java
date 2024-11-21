package com.santosfc.sitesantosfc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.santosfc.sitesantosfc.model.Titulo;
import com.santosfc.sitesantosfc.model.TituloService;
import com.santosfc.sitesantosfc.model.Tool;



@Controller
public class TitulosController {

    @Autowired
    private ApplicationContext context;

    private static String PASTA_UPLOAD = "src/main/resources/static/uploads/";
    

    @GetMapping("/")
    public String principal(){
       // retorno página principal
        return "principal";
    }

    @GetMapping("/escudos")
    public String escudos(){
       // retorno página principal
        return "escudos";
    }

    @GetMapping("/campeaoSerieB")
    public String campeaoSerieB(){
       // retorno página principal
        return "campeaoSerieB";
    }


    // ======================= Cadastrar Títulos =======================
    
    @GetMapping("/cadastroTitulo")
    public String cadastroTitulo(Model model){
        // atributo titulo é o nome do input da view cadastrarTitulo
        model.addAttribute("titulo", new Titulo("","","",""));
        // retorno página cadastro
        return "cadastroTitulo";
    }


    @PostMapping("/cadastroTitulo")
    public String cadastrarTitulo(Model model, @ModelAttribute Titulo titulo,
    @RequestParam("file") MultipartFile file){

        TituloService ts = context.getBean(TituloService.class);

        try { if (!file.isEmpty()) {

            // Define o caminho do diretório de upload
            Path uploadDir = Paths.get(PASTA_UPLOAD);

            // Verifica se o diretório de upload existe, caso contrário, cria-o
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                System.out.println("Diretório criado: " + uploadDir.toAbsolutePath());
            }


            // Salva o arquivo no diretório
            byte[] bytes = file.getBytes();
            Path path = uploadDir.resolve(file.getOriginalFilename());
            
            // Imprime o caminho absoluto no console para verificar
            System.out.println("Caminho absoluto do arquivo: " + path.toAbsolutePath());

            Files.write(path, bytes);
            titulo.setImagem(file.getOriginalFilename());

            // byte[] bytes = file.getBytes();
            // Path path = Paths.get(PASTA_UPLOAD + file.getOriginalFilename());
            // Files.write(path, bytes);
            // titulo.setImagem(file.getOriginalFilename());
            }
        } catch (IOException e) {
             e.printStackTrace(); 
        }

        ts.inserirTitulo(titulo);
        // arrumar depois o retorno para a página sucesso
        return "principal";
    }


    // =================================================================

    // *****************************************************************

    // ========================= Listar Títulos ========================

    @GetMapping("/listagemTitulo")
     public String listagemTitulo(Model model){

      TituloService ts = context.getBean(TituloService.class);

      // pegar uma lista no banco de dados usando a função obterTodosTitulos()
      //usando a variável "ts" com a classe TituloService"   
      List<Map<String, Object>> lista = ts.obterTodosTitulos();

      //Criar uma lista de Títulos Java para receber a lista de objetos JSON convertidos 
      // no "for" abaixo para objetos java utilizando a função da classe tool.java que criamos 
      // importar ArrayList
      //List é classe abstrata e Arraylist é classe concreta   
      List<Titulo> listaTitulos = new ArrayList<Titulo>();

      // Transforar a lista de JSON em lista de Titulos usando a classe Tool que criamos
      for(Map<String, Object> registro : lista){
          // importar a classe tool.java  
          listaTitulos.add(Tool.converterTitulo(registro));
      }
       //Mandar a listaClientes para o model   
       model.addAttribute("titulos", listaTitulos);
       return "listagemTitulo";
    }

    // =================================================================

    // *****************************************************************

    // ===================== Atualizar Título  =========================
    

    @GetMapping("/atualizarTitulo")
    public String showUpdateTitleForm(Model model) {
        // Logic to prepare data for the form, if needed
        model.addAttribute("titulo", new Titulo()); // Example: add an empty Titulo object
        return "atualizarTitulo"; // Assuming the template is in the templates directory
    }


    @GetMapping("/atualizarTitulo/{id}")
    // Variável de caminho @PasthVariable
    public String atualizarTitulo(Model model, @PathVariable int id){
        // Criar um cliente que não é vazio
        TituloService ts = context.getBean(TituloService.class);
        Titulo titulo = ts.obterTitulo(id);
        model.addAttribute("id", id);
        model.addAttribute("titulo", titulo);
        // retorno página cadastro
        return "atualizarTitulo";
    }


    @PostMapping("/atualizarTitulo/{id}")
    public String atualizarTitulo(@PathVariable int id, @ModelAttribute Titulo titulo,
    @RequestParam("file") MultipartFile file){
        TituloService ts = context.getBean(TituloService.class);
        Titulo old_img = ts.obterTitulo(id);
        String caminhoImagem = old_img.getImagem(); // Obter o caminho da imagem antiga
        Path path2 = Paths.get(PASTA_UPLOAD + caminhoImagem);


        try { if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(PASTA_UPLOAD + file.getOriginalFilename());
                Files.write(path, bytes);
                titulo.setImagem(file.getOriginalFilename());
                // Deletar o arquivo antigo se tiver imagem nova carregada
                Files.deleteIfExists(path2); 


             } else{
                // se não tiver img carregada seta o nome da img antiga para não ficar vazia
                titulo.setImagem(old_img.getImagem());
             }
        } catch (IOException e) {
             e.printStackTrace();
        }

        ts.atualizarTitulo(id, titulo);
        return "redirect:/listagemTitulo";
    }


    // =================================================================

    // *****************************************************************

    // =====================  Deletar Título ===========================

    @PostMapping("/deletarTitulo/{id}")
    public String deletarTitulo(@PathVariable int id){
        TituloService ts = context.getBean(TituloService.class);

        // apagar a imagem no servidor
        Titulo titulo = ts.obterTitulo(id);
        // Obter o caminho da imagem do título
        String caminhoImagem = titulo.getImagem(); 
            
        try {
            Files.deleteIfExists(Paths.get(PASTA_UPLOAD+ caminhoImagem)); 
        } catch (IOException e) {
            e.printStackTrace(); // Lidar com exceções
        }

        ts.deletarTitulo(id);
        return "redirect:/listagemTitulo";
       
    }

    
}
