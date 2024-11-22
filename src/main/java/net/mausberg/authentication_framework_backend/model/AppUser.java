package net.mausberg.authentication_framework_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "appusers")
public class AppUser extends AbstractAppUser{

	private static final long serialVersionUID = 1L;

}
