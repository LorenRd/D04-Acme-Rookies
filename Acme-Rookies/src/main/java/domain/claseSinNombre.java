
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class claseSinNombre extends DomainEntity {

	private String 	ticker;
	private Date	publicationMoment;
	private String	body;
	private String 	picture;
	private boolean isDraft;
	
	
	@NotBlank
	@Column(unique = true)
	//AAMMDD-ABCD
	//@Pattern(regexp = "^[0-9]{6}-([A-Z]{4})$") 
	//AAMMDD-1234
	@Pattern(regexp = "^[0-9]{6}-([0-9]{4})$") 
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getPublicationMoment() {
		return this.publicationMoment;
	}
	public void setPublicationMoment(final Date publicationMoment) {
		this.publicationMoment = publicationMoment;
	}
	
	@Size(max = 100)
	@NotBlank
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	@URL
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}
	
	public boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}
	
	// Relationships----------------------------------------------
	private Audit		audit;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Audit getAudit() {
		return this.audit;
	}

	public void setAudit(final Audit audit) {
		this.audit = audit;
	}
	
}