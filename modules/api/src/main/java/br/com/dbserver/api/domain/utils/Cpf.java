package br.com.dbserver.api.domain.utils;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

@Embeddable
public class Cpf {

    @NotBlank(message = "O campo 'cpf' é obrigatório.")
    @CPF(message = "CPF inválido.")
    private String currentCpf;

    protected Cpf() {}

    public Cpf(String currentCpf) {
        this.currentCpf = unformatCpf(currentCpf);
    }

    public String getValue() {
        return currentCpf;
    }

    public String getFormatted() {
        var n = currentCpf;
        if (n == null || n.length() != 11) 
            return n;
        return n.substring(0,3)+"."+n.substring(3,6)+"."+n.substring(6,9)+"-"+n.substring(9);
    }

    private static String unformatCpf(String s) {
        return s == null ? null : s.replaceAll("\\D", "");
    }

    @Override 
    public String toString() { 
        return getFormatted(); 
    }

    @Override 
    public boolean equals(Object o) { 
        return super.equals(o); 
    }
    
    @Override 
    public int hashCode() { 
        return super.hashCode(); 
    }
}
