/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.sgi.ejb;

import javax.ejb.Remote;

/**
 *
 * @author intel core i 7
 */
@Remote
public interface MailManagerRemote {

    void sendMail(String body);
    
}
