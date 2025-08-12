package br.com.dbserver.api.domain.utils.type;

import br.com.dbserver.api.domain.utils.constants.ValidationMessages;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Objects;

/**
 * Representa um CPF (Cadastro de Pessoa Física) brasileiro.
 * 
 * Esta classe encapsula a validação, formatação e manipulação do CPF, garantindo
 * que o valor armazenado esteja sempre sem formatação e válido segundo as regras brasileiras.
 * 
 * Implementada como uma classe <em>embeddable</em> para ser utilizada como parte de
 * outras entidades JPA, facilitando o reuso e a padronização do tratamento de CPFs no sistema.
 * 
 */
@Embeddable
public class Cpf {

    @NotBlank(message = ValidationMessages.MEMBRO_CPF_OBRIGATORIO)
    @CPF(message = ValidationMessages.MEMBRO_CPF_INVALIDO)
    private String currentCpf;

    protected Cpf() {}

    /**
     * Constrói uma instância de {@code Cpf} a partir de uma string,
     * removendo qualquer formatação presente (pontos, traços etc).
     * 
     * @param currentCpf CPF como string, formatado ou não
     */
    public Cpf(String currentCpf) {
        this.currentCpf = unformatCpf(currentCpf);
    }

    /**
     * Retorna o valor do CPF sem formatação (apenas números).
     * 
     * @return CPF sem formatação
     */
    public String getValue() {
        return currentCpf;
    }

    /**
     * Retorna o CPF formatado no padrão brasileiro: {@code XXX.XXX.XXX-XX}.
     * Se o valor não possuir 11 dígitos, retorna o valor bruto.
     * 
     * @return CPF formatado ou valor bruto se inválido
     */
    public String getFormatted() {
        var n = currentCpf;
        if (n == null || n.length() != 11) 
            return n;
        return n.substring(0,3)+"."+n.substring(3,6)+"."+n.substring(6,9)+"-"+n.substring(9);
    }

    /**
     * Remove toda a formatação da string do CPF,
     * retornando apenas os dígitos numéricos.
     * 
     * @param s CPF possivelmente formatado
     * @return CPF sem formatação ou {@code null} se a entrada for {@code null}
     */
    private static String unformatCpf(String s) {
        return s == null ? null : s.replaceAll("\\D", "");
    }

    @Override 
    public String toString() { 
        return getFormatted(); 
    }

    @Override 
    public boolean equals(Object o) { 
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cpf cpf = (Cpf) o;
        return Objects.equals(currentCpf, cpf.currentCpf);
    }
    
    @Override 
    public int hashCode() { 
        return Objects.hash(currentCpf); 
    }
}
