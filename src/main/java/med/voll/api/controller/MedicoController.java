package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController // Controlador especial usado para serviços RESTFul e o equivalente a @Controller + @ResponseBody.
@RequestMapping("medicos") // Usada para mapear solicitações da web para classes manipuladoras especificas e métodos manipuladores
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping //O controller vai lidar com o verbo POST para a URL /medicos
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        // O URI devolve o endereço do servidor. Como pode mudar a cada local que abrimos, o URI Builder devovle o endereço local como padrão
        var medico = new Medico(dados);
        repository.save(medico);
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico)); // Será devolvido o código 201

    }

    @GetMapping //O controler vai lidar com o verbo get para a URL /medicos
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) { //Paginação
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

//    @DeleteMapping("/{id}") // Parêmetro dinâmico para chamar o Delete // DELETOU, mas queremos exclusão lógica (não apagar)
//    @Transactional
//    public void excluir(@PathVariable Long id){
//        repository.deleteById(id);
//    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){ // A classe Response Entity é do próprio Springe ajuda
        // a configurar qual o código devolvido de cada requisição.
        var medico = repository.getReferenceById(id);
        medico.excluir(); // Foi feito a exclusão lógica, ou seja, não apagou do DB, apenas ocultou
        return ResponseEntity.noContent().build(); // Será devolvido o código 204.
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico)); //
    }


}
