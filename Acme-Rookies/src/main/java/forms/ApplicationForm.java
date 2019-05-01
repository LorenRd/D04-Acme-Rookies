
package forms;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Rookie;
import domain.Position;
import domain.Problem;

public class ApplicationForm {

	private Date	moment;
	private String	status;


	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@Pattern(regexp = "^PENDING|SUBMITTED|APPROVED|REJECTED$")
	@SafeHtml
	public String getStatus() {
		return this.status;
	}
	public void setStatus(final String status) {
		this.status = status;
	}


	private Problem		problem;
	private Rookie		rookie;
	private Position	position;
	private Date		answerMoment;
	private String		answerText;
	private String		codeLink;
	private int			id;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getAnswerMoment() {
		return this.answerMoment;
	}
	public void setAnswerMoment(final Date answerMoment) {
		this.answerMoment = answerMoment;
	}

	@SafeHtml
	public String getAnswerText() {
		return this.answerText;
	}
	public void setAnswerText(final String answerText) {
		this.answerText = answerText;
	}

	@URL
	@SafeHtml
	public String getCodeLink() {
		return this.codeLink;
	}
	public void setCodeLink(final String codeLink) {
		this.codeLink = codeLink;
	}

	@Valid
	@ManyToOne(optional = false)
	public Problem getProblem() {
		return this.problem;
	}

	public void setProblem(final Problem problem) {
		this.problem = problem;
	}

	@Valid
	@ManyToOne(optional = false)
	public Rookie getRookie() {
		return this.rookie;
	}

	public void setRookie(final Rookie rookie) {
		this.rookie = rookie;
	}

	@Valid
	@ManyToOne(optional = false)
	public Position getPosition() {
		return this.position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

}
