package ru.dipech.resumes;

import ru.dipech.resumes.model.Resume;
import ru.dipech.resumes.util.ResumeTestDataUtil;

public class MainResume {

    public static void main(String[] args) {
        Resume resumeLowFilled = ResumeTestDataUtil.createWithTestData("Min Minimum", "01", ResumeTestDataUtil.FillOption.LOW);
        Resume resumeMediumFilled = ResumeTestDataUtil.createWithTestData("Avg Average", "02", ResumeTestDataUtil.FillOption.MEDIUM);
        Resume resumeHighFilled = ResumeTestDataUtil.createWithTestData("Max Maximum", "03", ResumeTestDataUtil.FillOption.HIGH);
        System.out.println(resumeLowFilled);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println(resumeMediumFilled);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println(resumeHighFilled);
    }

}
