package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.DadosListagemPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController // Controlador especial usado para serviços RESTFul e o equivalente a @Controller + @ResponseBody.
@RequestMapping("pacientes") // Usada para mapear solicitações da web para classes manipuladoras especificas e métodos manipuladores
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping //O controller vai lidar com o verbo POST para a URL /pacientes
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
        // O URI devolve o endereço do servidor. Como pode mudar a cada local que abrimos, o URI Builder devovle o endereço local como padrão
        var paciente = new Paciente(dados);
        repository.save(paciente);
        var uri = uriBuilder.path("/pacientes   /{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente)); // Será devolvido o código 201
    }

    @GetMapping //O controler vai lidar com o verbo get para a URL /medicos
    public ResponseEntity<Page<DadosListagemPaciente>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) { //Paginação
        var page = repository.findAll(paginacao).map(DadosListagemPaciente::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados){
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id){ // A classe Response Entity é do próprio Springe ajuda
        // a configurar qual o código devolvido de cada requisição.
        var paciente = repository.getReferenceById(id);
        paciente.excluir(); // Foi feito a exclusão lógica, ou seja, não apagou do DB, apenas ocultou
        return ResponseEntity.noContent().build(); // Será devolvido o código 204.
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente)); //
    }
}