/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.dataprocessor;

/**
 *
 * @author yoga
 */
public interface IDataprocessorPipelineHook {
    public void willExecute();
    public void didExecute();
}
