(setq initial-frame-alist '((foreground-color . "grey90")
			    (background-color . "grey20")
			    (left . 50)  ))
(setq default-frame-alist '((foreground-color . "grey90")
			    (background-color . "grey20")
			    (cursor-color . "green")
			    (left . 0)
			    (width . 141)
			    (height . 44)))
(set-default 'cursor-type 'box)


;;(R-mode 1)

;;(setq ess-history-directory "~")
;;(ess-toggle-underscore nil)

(global-set-key (kbd "M-[") 'insert-pair)
(global-set-key (kbd "M-\"") 'insert-pair)
(custom-set-variables
 ;; custom-set-variables was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 '(inhibit-startup-screen t)
 '(send-mail-function nil))
(custom-set-faces
 ;; custom-set-faces was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 )


;; (defun then_R_operator ()
;;   "R - %>% operator or 'then' pipe operator"
;;   (interactive)
;;   (just-one-space 1)
;;   (insert "%>%")
;;   (reindent-then-newline-and-indent))
;; (define-key ess-mode-map (kbd "C-%") 'then_R_operator)
;; (define-key inferior-ess-mode-map (kbd "C-%") 'then_R_operator)


;; ----------------------------------------------------------------------
;; Brilliant:
;; https://github.com/emacs-ess/ESS/issues/96#issuecomment-71984227
;; (add-to-list 'ess-style-alist
;;              '(my-style
;;                (ess-indent-level . 4)
;;                (ess-first-continued-statement-offset . 0) ;; should it be 2?
;;                (ess-continued-statement-offset . 0)
;;                (ess-brace-offset . -4)
;;                (ess-expression-offset . 4)
;;                (ess-else-offset . 0)
;;                (ess-close-brace-offset . 0)
;;                (ess-brace-imaginary-offset . 0)
;;                (ess-continued-brace-offset . 0)
;;                (ess-arg-function-offset . 4)
;;            (ess-arg-function-offset-new-line . '(4))
;;                ))
;; (setq ess-default-style 'my-style)
;; ----------------------------------------------------------------------

;; ----------------------------------------------------------------------

;; ----------------------------------------------------------------------
;; Brilliant:
;; https://github.com/emacs-ess/ESS/issues/96#issuecomment-71984227
(add-to-list 'ess-style-alist
             '(my-style
               (ess-indent-level . 4)
               (ess-first-continued-statement-offset . 2) ;; should it be 2? yes
               (ess-continued-statement-offset . 0)
               (ess-brace-offset . -4)
               (ess-expression-offset . 4)
               (ess-else-offset . 0)
               (ess-close-brace-offset . 0)
               (ess-brace-imaginary-offset . 0)
               (ess-continued-brace-offset . 0)
               (ess-arg-function-offset . 4)
           (ess-arg-function-offset-new-line . '(4))
               ))
(setq ess-default-style 'my-style)

;; (setq paragraph-start
;;       '("\f\\|[ \t]*$\\|[ \t]*[*+-] \\|[ \t]*[0-9]+\\.\\|[ \t]*: "))

;; (setq paragraph-separate
;;       '("\\(?:[ \t\f]\\|.*  \\)*$"))


;; Paragraph filling
(set (make-local-variable 'paragraph-start)
       "\f\\|[ \t]*$\\|[ \t]*[*+-] \\|[ \t]*[0-9]+\\.\\|[ \t]*: ")
(set (make-local-variable 'paragraph-separate)
     "\\(?:[ \t\f]\\|.*  \\)*$")
(set (make-local-variable 'adaptive-fill-first-line-regexp)
     "\\`[ \t]*>[ \t]*?\\'")
(set (make-local-variable 'adaptive-fill-function)
     'markdown-adaptive-fill-function)
(put 'upcase-region 'disabled nil)


;; use linux line endings
(setq default-buffer-file-coding-system 'utf-8-unix)


(defun renumber (&optional num)
  "Renumber the list items in the current paragraph,
    starting at point."
  (interactive "p")
  (setq num (or num 1))
  (let ((end (save-excursion
	       (forward-paragraph)
	       (point))))
    (while (re-search-forward "^[0-9]+" end t)
      (replace-match (number-to-string num))
      (setq num (1+ num)))))

(defun renumber-list (start end &optional num)
  "Renumber the list items in the current START..END region.
    If optional prefix arg NUM is given, start numbering from that number
    instead of 1."
  (interactive "*r\np")
  (save-excursion
    (goto-char start)
    (setq num (or num 1))
    (save-match-data
      (while (re-search-forward "^[0-9]+" end t)
	(replace-match (number-to-string num))
	(setq num (1+ num))))))


;; ----------------------------------------------------------------------
;; https://kb.iu.edu/d/abuf
;; display column numbers below buffer
(setq column-number-mode t)
;; ----------------------------------------------------------------------

(defun bigcomment ()
  (interactive)
  (insert "#### ----------------------------------------------------------------------\n")
  (insert "#### \n")
  (insert "#### ----------------------------------------------------------------------\n")
  (backward-char 77))

(defun ligcomment ()
  (interactive)
  (insert "## ----------------------------------------------------------------------\n")
  (insert "## \n")
  (insert "## ----------------------------------------------------------------------\n")
  (backward-char 75))


;; (require' package)
;; (add-to-list 'package-archives
;; 	     '("melpa" . "http://melpa.milkbox.net/packages/") t)

;; (setq exec-path (append exec-path '("/usr/local/share/scala-2.11.7")))

;;(require 'ensime)
;;(add-hook 'scala-mode-hook 'ensime-scala-mode-hook)

(add-to-list 'load-path "/home/steve/scala-tool-support/tool-support/emacs")
(add-to-list 'load-path "/usr/local/share/scala-2.11.7")
(require 'scala-mode-auto)

(defun essSparkR ()
  (interactive)
  (R)
  (insert "Sys.setenv\(SPARK_HOME = \"/usr/local/share/spark-1.6.0-bin-hadoop2.6\"); ")
  (insert "library(SparkR, lib = file.path(Sys.getenv(\"SPARK_HOME\"), \"R\", \"lib\")); ")
  (insert "sc <- sparkR.init(sparkPackages=\"com.databricks:spark-csv_2.11:1.0.3\"); ")
  (insert "sqlContext <- sparkRSQL.init(sc)")
  (inferior-ess-send-input))


(defun bigcomment ()
  (interactive)
  (insert "#### ----------------------------------------------------------------------\n")
  (insert "#### \n")
  (insert "#### ----------------------------------------------------------------------\n")
  (backward-char 77))

(defun ligcomment ()
  (interactive)
  (insert "## ----------------------------------------------------------------------\n")
  (insert "## \n")
  (insert "## ----------------------------------------------------------------------\n")
  (backward-char 75))



(defun rpipe ()
  (interactive)
  (just-one-space 1)
  (insert "%>%")
  (just-one-space 1))

(define-key ess-mode-map (kbd "C-%") 'rpipe)
(define-key inferior-ess-mode-map (kbd "C-%") 'rpipe)
