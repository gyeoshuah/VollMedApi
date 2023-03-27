package med.voll.api.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroPaciente (

    @NotBlank // Não pode ser nulo nem vazio, precisa ter um texto String
    String nome,

    @NotBlank
    @Email // Padrão email
    String email,

    @NotBlank
    String telefone,

    @NotBlank
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}") // Para dígitos
    String cpf,

    @NotNull
    @Valid // Valida porque o endereço também tem um DTO com outras validações
    DadosEndereco endereco) {
    }

