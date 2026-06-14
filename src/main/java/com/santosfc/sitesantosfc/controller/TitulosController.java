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

import com.santosfc.sitesantosfc.model.Campeonato;
import com.santosfc.sitesantosfc.model.CampeonatoService;
import com.santosfc.sitesantosfc.model.CloudinaryService;
import com.santosfc.sitesantosfc.model.Jogador;
import com.santosfc.sitesantosfc.model.JogadorService;
import com.santosfc.sitesantosfc.model.Titulo;
import com.santosfc.sitesantosfc.model.TituloService;
import com.santosfc.sitesantosfc.model.Tool;
import com.santosfc.sitesantosfc.model.Usuario;
//import com.santosfc.sitesantosfc.model.UsuarioDAO;
import com.santosfc.sitesantosfc.model.UsuarioService;


@Controller
public class TitulosController {

    @Autowired
    private ApplicationContext context;

    private static String PASTA_UPLOAD = "src/main/resources/static/uploads/";
   
    
    // ======================= Páginas públicas =======================

    @GetMapping("/")
    public String principal(){
       // retorno página principal
        return "principal";
    }

    @GetMapping("/elenco")
    public String elenco(){
       // retorno página elenco
        return "elenco";
    }

    @GetMapping("/escudos")
    public String escudos(){
       // retorno página escudos
        return "escudos";
    }

 

    // ======================= Cadastrar Usuário =======================

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String formCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastroUsuario";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@ModelAttribute Usuario usuario) {
        UsuarioService us = context.getBean(UsuarioService.class);
        us.inserirUsuario(usuario);                      // salva com BCrypt
        String uuid = us.obterUUID(usuario.getEmail());  // busca UUID gerado
        us.inserirPerfil(uuid);                          // atribui cargo "torcedor"
        return "redirect:/login";
    }

    @GetMapping("/cadastroUsuario")
    public String formCadastroUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastroUsuario";
    }

    // ← ADICIONAR ESTE MÉTODO
    @PostMapping("/cadastroUsuario")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario) {
        UsuarioService us = context.getBean(UsuarioService.class);
        us.inserirUsuario(usuario);
        String uuid = us.obterUUID(usuario.getEmail());
        us.inserirPerfil(uuid);
        return "redirect:/login";
    }

    // Listagem só para admin
    @GetMapping("/listagemUsuario")
    public String listarUsuarios(Model model) {
        UsuarioService us = context.getBean(UsuarioService.class);
        model.addAttribute("usuarios", us.listarUsuarios());
        return "listagemUsuario";
    }

    //Acesso negado de usuário comum para ver listagem de usuários
    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acessoNegado";
    }

    //Deletar Usuário
    @PostMapping("/deletarUsuario/{id}")
    public String deletarUsuario(@PathVariable String id) {
        UsuarioService us = context.getBean(UsuarioService.class);
        us.deletarUsuario(id);
        return "redirect:/listagemUsuario";
    }
    

    // ======================= Jogador =======================

    @GetMapping("/jogador")
    public String listarJogadores(Model model) {
        JogadorService js = context.getBean(JogadorService.class);
        model.addAttribute("jogadores", js.listarJogadores());
        return "listagemJogador";
    }

    @GetMapping("/jogador/cadastrar")
    public String formJogador(Model model) {
        model.addAttribute("jogador", new Jogador());
        return "cadastroJogador";
    }

    @PostMapping("/jogador/cadastrar")
    public String cadastrarJogador(@ModelAttribute Jogador jogador, Model model) {
    try {
        JogadorService js = context.getBean(JogadorService.class);
        js.inserirJogador(jogador);
        return "redirect:/jogador";
    } catch (Exception e) {
        model.addAttribute("jogador", jogador);
        model.addAttribute("erro", "Matrícula " + jogador.getMatricula() + " já existe!");
        return "cadastroJogador";
    }
}

    @PostMapping("/jogador/{id}/deletar")
    public String deletarJogador(@PathVariable String id) {
        JogadorService js = context.getBean(JogadorService.class);
        js.deletarJogador(id);
        return "redirect:/jogador";
    }

    // ======================= Campeonato =======================

    @GetMapping("/campeonato")
    public String listarCampeonatos(Model model) {
        CampeonatoService cs = context.getBean(CampeonatoService.class);
        model.addAttribute("campeonatos", cs.listarCampeonatos());
        return "listagemCampeonato";
    }

    @GetMapping("/campeonato/cadastrar")
    public String formCampeonato(Model model) {
        model.addAttribute("campeonato", new Campeonato());
        return "cadastroCampeonato";
    }

    @PostMapping("/campeonato/cadastrar")
    public String cadastrarCampeonato(@ModelAttribute Campeonato campeonato) {
        CampeonatoService cs = context.getBean(CampeonatoService.class);
        cs.inserirCampeonato(campeonato);
        return "redirect:/campeonato";
    }

    @PostMapping("/campeonato/{id}/deletar")
    public String deletarCampeonato(@PathVariable String id) {
        CampeonatoService cs = context.getBean(CampeonatoService.class);
        cs.deletarCampeonato(id);
        return "redirect:/campeonato";
    }

    // ======================= Inscrição =======================

    @GetMapping("/inscricao")
    public String formInscricao(Model model) {
        JogadorService js = context.getBean(JogadorService.class);
        CampeonatoService cs = context.getBean(CampeonatoService.class);
        model.addAttribute("jogadores", js.listarJogadores());
        model.addAttribute("campeonatos", cs.listarCampeonatos());
        return "inscricao";
    }

    @PostMapping("/inscricao")
    public String inscrever(@RequestParam String jogadorId,
                            @RequestParam String campeonatoId) {
        CampeonatoService cs = context.getBean(CampeonatoService.class);
        cs.inscrever(jogadorId, campeonatoId);
        return "redirect:/campeonato";
    }

    // ======================= Inscritos =======================

    @GetMapping("/inscritos")
    public String listarInscritos(Model model) {
        JogadorService js = context.getBean(JogadorService.class);
        model.addAttribute("inscritos", js.listarInscritos());
        return "listagemInscritos";
    }


    // ======================= Cadastrar Títulos =======================
    
    @GetMapping("/cadastroTitulo")
    public String cadastroTitulo(Model model){
        // atributo titulo é o nome do input da view cadastrarTitulo
        model.addAttribute("titulo", new Titulo("","","",""));
        // retorno página cadastro
        return "cadastroTitulo";
    }

    /* 
    @PostMapping("/cadastroTitulo")
    public String cadastrarTitulo(Model model, @ModelAttribute Titulo titulo,
    @RequestParam("file") MultipartFile file){

        TituloService ts = context.getBean(TituloService.class);

        try { if (!file.isEmpty()) {

            // Verifica se o diretório de upload existe, caso contrário, cria-o
            Path abs = Paths.get(PASTA_UPLOAD);
            System.out.println("Caminho absoluto do arquivo: " + abs.toAbsolutePath());
     
            byte[] bytes = file.getBytes();
            Path path = Paths.get(PASTA_UPLOAD + file.getOriginalFilename());
            Files.write(path, bytes);
            titulo.setImagem(file.getOriginalFilename());

            }
        } catch (IOException e) {
             e.printStackTrace(); 
          }

        ts.inserirTitulo(titulo);
        // arrumar depois o retorno para a página sucesso
        //return "principal";
        return "redirect:/listagemTitulo";  // ← corrigido: era "principal"
    }
    */

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/cadastroTitulo")
    public String cadastrarTitulo(Model model, @ModelAttribute Titulo titulo,
            @RequestParam("file") MultipartFile file) {
        TituloService ts = context.getBean(TituloService.class);
        try {
            if (!file.isEmpty()) {
                String urlImagem = cloudinaryService.uploadImagem(file);
                titulo.setImagem(urlImagem); // salva a URL em vez do nome do arquivo
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ts.inserirTitulo(titulo);
        return "redirect:/listagemTitulo";
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

    /*
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
    */

    @PostMapping("/atualizarTitulo/{id}")
    public String atualizarTitulo(@PathVariable int id, @ModelAttribute Titulo titulo,
            @RequestParam("file") MultipartFile file) {
        TituloService ts = context.getBean(TituloService.class);
        Titulo oldTitulo = ts.obterTitulo(id);
        try {
            if (!file.isEmpty()) {
                // Deleta a imagem antiga do Cloudinary
                if (oldTitulo.getImagem() != null && oldTitulo.getImagem().startsWith("http")) {
                    cloudinaryService.deletarImagem(oldTitulo.getImagem());
                }
                String urlImagem = cloudinaryService.uploadImagem(file);
                titulo.setImagem(urlImagem);
            } else {
                titulo.setImagem(oldTitulo.getImagem());
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

    
    /*
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
    */

    @PostMapping("/deletarTitulo/{id}")
        public String deletarTitulo(@PathVariable int id) {
            TituloService ts = context.getBean(TituloService.class);
            Titulo titulo = ts.obterTitulo(id);
                // Deleta do Cloudinary se for URL
                if (titulo.getImagem() != null && titulo.getImagem().startsWith("http")) {
                    cloudinaryService.deletarImagem(titulo.getImagem());
                }

            ts.deletarTitulo(id);
            return "redirect:/listagemTitulo";
        }

    
}


