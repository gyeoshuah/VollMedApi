package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
        @NotBlank // Não pode ser nulo nem vazio, precisa ter um texto String
        String nome,

        @NotBlank
        @Email // Padrão email
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{4,6}") // Para dígitos
        String crm,

        @NotNull // Não pode ser nulo
        Especialidade especialidade,

        @NotNull
        @Valid // Valida porque o endereço também tem um DTO com outras validações
        DadosEndereco endereco) {
}
