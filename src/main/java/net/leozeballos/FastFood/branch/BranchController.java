package net.leozeballos.FastFood.branch;

import net.leozeballos.FastFood.address.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BranchController {

    BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping(value = "/branch/{id}", produces = "application/json")
    public @ResponseBody
    Branch getBranch(@PathVariable("id") Long id) {
        return branchService.findById(id);
    }

    @RequestMapping("/branch/delete/{id}")
    public String deleteBranch(@PathVariable("id") Long id) {
        branchService.deleteById(id);
        return "redirect:/branch/list";
    }

    @RequestMapping("/branch/edit/{id}")
    public ModelAndView editBranch(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("branch/edit_branch");
        mav.addObject("branch", branchService.findById(id));
        mav.addObject("pageTitle", "Edit Branch");
        return mav;
    }

    @RequestMapping("/branch/new")
    public String newBranch(Model model) {
        Branch branch = new Branch();
        branch.setAddress(new Address());
        model.addAttribute("branch", branch);
        model.addAttribute("pageTitle", "New Branch");
        return "branch/new_branch";
    }

    @RequestMapping("/branch/save")
    public String saveBranch(@ModelAttribute("branch") Branch branch) throws RuntimeException {
        if (branchService.save(branch) != null) {
            return "redirect:/branch/list";
        } else {
            throw new RuntimeException("Error saving branch");
        }
    }

    @RequestMapping("/branch/list")
    public String listBranches(Model model) {
        model.addAttribute("listBranches", branchService.findAll());
        model.addAttribute("pageTitle", "Branches List");
        return "branch/list_branch";
    }

}
