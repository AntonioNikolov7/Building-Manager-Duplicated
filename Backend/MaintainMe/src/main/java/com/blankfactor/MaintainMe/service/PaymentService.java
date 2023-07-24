package com.blankfactor.MaintainMe.service;

import com.blankfactor.MaintainMe.entity.Invoice;
import com.blankfactor.MaintainMe.entity.Payment;
import com.blankfactor.MaintainMe.entity.User;
import com.blankfactor.MaintainMe.paymentProcessor.PaymentProcessor;
import com.blankfactor.MaintainMe.repository.InvoiceRepository;
import com.blankfactor.MaintainMe.repository.LocalUserRepository;
import com.blankfactor.MaintainMe.repository.PaymentRepository;
import com.blankfactor.MaintainMe.web.resource.PaymentHistoryRequest;
import com.blankfactor.MaintainMe.web.resource.PaymentRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@EnableScheduling

public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final EmailService emailService;
    private final LocalUserRepository userRepository;
    private final PaymentProcessor paymentProcessor;
    private final JWTService jwtService;

    @PersistenceContext
    private final EntityManager entityManager;

    public Payment makePayment(PaymentRequest paymentRequest) {

        String email =  jwtService.getEmail(paymentRequest.getToken());
        User authUser = userRepository.getUserByEmail(email);

        Date date = new Date();

        Invoice invoice = invoiceRepository.getInvoiceById(paymentRequest.getInvoiceId());

        try {

            if(paymentRequest.getPayedAmount() >0 && paymentRequest.getPayedAmount() <= invoice.getTotalAmount() ){

                var payment = Payment.builder()
                        .paymentAmount(paymentRequest.getPayedAmount())
                        .user(authUser)
                        .invoice(invoice)
                        .date(date)
                        .build();

                paymentProcessor.makePayment(payment);
                paymentRepository.save(payment);

                invoice.setTotalAmount(invoice.getTotalAmount() - payment.getPaymentAmount());
                invoiceRepository.save(invoice);

                String emailSubject = "Payment was made to invoice with ID: " + invoice.getId();
                String emailText = emailSubject + "\n Invoice info: \n Total left: " +invoice.getTotalAmount() + "\n Due date: " + invoice.getDueDate()
                        + "\n Invoice info: " + invoice.getInvoiceInfo();

                emailService.sendEmail(authUser.getEmail(),emailSubject, emailText);

                if(invoice.getTotalAmount() ==0){
                    invoice.setIsFullyPaid(true);
                    invoiceRepository.save(invoice);
                }

            }else {
                throw new Exception("Payed amount should be more than 0 or less than total owed");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Payment> getPaymentHistory(PaymentHistoryRequest paymentHistoryRequest){

        String email =  jwtService.getEmail(paymentHistoryRequest.getToken());
        User authUser = userRepository.getUserByEmail(email);

        return paymentRepository.findAllByUserId(authUser.getId());
    }

    public void autoPayment() {
        
        final int PAGE_SIZE = 100;
        int page = 0;

        Slice<User> userPage;
        do {
            userPage = userRepository.findAllByAutoPay(PageRequest.of(page, PAGE_SIZE));
            for (User user : userPage) {
                System.out.println(user.getEmail());

                List<Invoice> unpaidInvoices = invoiceRepository.findUnpaidInvoices(user.getUnit().getId());

                System.out.println("unpaid invoices for user with id: " + user.getId() + "   :" + unpaidInvoices.size());

                for (int j = 0; j < unpaidInvoices.size(); j++) {

                    Date date = new Date();

                    var payment = Payment.builder()
                            .paymentAmount(unpaidInvoices.get(j).getTotalAmount())
                            .user(user)
                            .invoice(unpaidInvoices.get(j))
                            .date(date)
                            .build();

                    paymentProcessor.makePayment(payment);
                    paymentRepository.save(payment);

                    unpaidInvoices.get(j).setTotalAmount(0F);
                    unpaidInvoices.get(j).setIsFullyPaid(true);

                    invoiceRepository.save(unpaidInvoices.get(j));

                }
                entityManager.clear();
                page++;
            }
        }while (userPage.hasNext());

             }
        }


















