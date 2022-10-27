package com.yzy.service.impl;

import com.yzy.entity.FtpTask;
import com.yzy.service.FtpService;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class FtpServiceImpl implements FtpService {

    @Override
    public void uploadFilesToFtp(FtpTask ftpTask, List<String> filePaths) throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        ftpClient.connect(ftpTask.getFtp_host(), ftpTask.getFtp_port());
        ftpClient.login(ftpTask.getFtp_user(), ftpTask.getFtp_pwd());
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        ftpClient.makeDirectory(ftpTask.getFile_save_catalog());
        ftpClient.changeWorkingDirectory(ftpTask.getFile_save_catalog());
        ftpClient.enterLocalPassiveMode();
        for (String filePath: filePaths){
            File file = new File(filePath);
            InputStream inputStream = new FileInputStream(file);
            ftpClient.storeFile(file.getName(), inputStream);
            inputStream.close();
        }

        ftpClient.logout();
        ftpClient.disconnect();
    }
}
