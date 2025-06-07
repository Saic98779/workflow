package com.metaverse.workflow.model;

import com.metaverse.workflow.common.enums.ExpenditureType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "program_expenditure")
public class ProgramExpenditure {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="program_expenditure_id")
        private Long programExpenditureId;

        @ManyToOne
        @JoinColumn(name = "activity_id")
        private Activity activity;

        @ManyToOne
        @JoinColumn(name = "sub_activity_id")
        private SubActivity subActivity;

        @ManyToOne
        @JoinColumn(name = "program_id")
        private Program program;

        @ManyToOne
        @JoinColumn(name = "agency_id")
        private Agency agency;

        @Column(name = "expenditure_type")
        private ExpenditureType expenditureType;

        @OneToMany(mappedBy = "programExpenditure", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ProgramSessionFile> programExpenditureFileList;

        @ManyToOne
        @JoinColumn(name="expense_id")
        private HeadOfExpense headOfExpense;

        @Column(name = "cost")
        private Double cost;

        @Column(name = "bill_no")
        private String billNo;

        @Column(name = "bill_date")
        private Date billDate;

        @Column(name = "payee_name")
        private String payeeName;

        @Column(name = "bank_name")
        private String bankName;

        @Column(name = "ifsc_code")
        private String ifscCode;

        @Column(name = "mode_of_payment")
        private String modeOfPayment;

        @Column(name = "transaction_id")
        private String transactionId;//for upi

        @Column(name = "purpose")
        private String purpose;

        @Column(name = "bill_url")
        private String uploadBillUrl;

        @Column(name="created_on", insertable = true, updatable = false)
        @CreationTimestamp
        private Date createdOn;

        @Column(name="updated_on", insertable = false, updatable = true)
        @UpdateTimestamp
        private Date updatedOn;
}
