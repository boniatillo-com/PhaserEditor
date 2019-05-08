package phasereditor.ide.ui.views;

import static phasereditor.ui.PhaserEditorUI.swtRun;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.Document;

import phasereditor.lic.HttpTool;
import phasereditor.project.ui.wizards.NewPhaserExampleProjectWizard;
import phasereditor.project.ui.wizards.NewPhaserProjectWizard;
import phasereditor.ui.EditorSharedImages;
import phasereditor.ui.SwtRM;
import phasereditor.webrun.ui.WebRunUI;

public class StartView extends ViewPart {

	private Composite _mainComp;
	private ScrolledComposite _scrolledComp;

	public StartView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		_scrolledComp = new ScrolledComposite(parent, SWT.V_SCROLL);
		_mainComp = new Composite(_scrolledComp, SWT.H_SCROLL);
		var layout = new GridLayout(2, true);
		layout.marginWidth = 50;
		layout.marginHeight = 50;
		_mainComp.setLayout(layout);

		createMainHeader(_mainComp);

		createLeftComp();

		createRightComp();

		_scrolledComp.setContent(_mainComp);
		_scrolledComp.setExpandVertical(true);
		_scrolledComp.setExpandHorizontal(true);
		_scrolledComp.addControlListener(ControlListener.controlResizedAdapter(e -> {
			updateScrolledComposite();
		}));

	}

	private void updateScrolledComposite() {
		Rectangle r = _scrolledComp.getClientArea();
		_scrolledComp.setMinSize(_mainComp.computeSize(r.width, SWT.DEFAULT));
	}

	private void createRightComp() {

		var parent = new Composite(_mainComp, SWT.NONE);
		parent.setLayout(new GridLayout(1, true));
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		createHSpace(parent);

		createHeader(parent, "Social Channels");

		createSocialChannelsComp(parent);

		createHSpace(parent);

		createNewsComp(parent);
	}

	private void createNewsComp(Composite parent) {
		createHeader(parent, "Latest News");

		var comp = new Composite(parent, SWT.NONE);
		var layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		comp.setLayout(layout);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));

		var job = new Job("Fetching latest news") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					var feedUrl = "https://phasereditor2d.com/blog/feed/rss";
					
					if (System.getProperty("localfeed") != null) {
						feedUrl = "http://localhost/blog/feed/rss";
					}
					
					var content = HttpTool.GET(feedUrl);

					swtRun(() -> {
						try {
							DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
							DocumentBuilder builder;

							builder = factory.newDocumentBuilder();

							Document doc = builder.parse(new ByteArrayInputStream(content.getBytes()));
							doc.getDocumentElement().normalize();

							var titleList = doc.getElementsByTagName("title");
							var linkList = doc.getElementsByTagName("link");
							var descriptionList = doc.getElementsByTagName("description");
							var pubDateList = doc.getElementsByTagName("pubDate");

							for (int i = 1; i < titleList.getLength(); i++) {
								if (i > 5) {
									break;
								}
								var title = titleList.item(i).getTextContent();
								var link = linkList.item(i).getTextContent();
								var description = descriptionList.item(i).getTextContent();
								var pubDate = pubDateList.item(i - 1).getTextContent();

								createLink(comp, title, createWebLinkRunnable("https://phasereditor2d.com" + link));

								var descText = new Text(comp, SWT.WRAP | SWT.MULTI);
								descText.setText(description);
								var gd = new GridData(GridData.FILL_BOTH);
								gd.minimumHeight = 50;
								descText.setLayoutData(gd);
								var fd = comp.getFont().getFontData()[0];
								createLabel(comp, pubDate).setFont(SwtRM.getFont(fd.name, fd.getHeight(), SWT.ITALIC));
							}

							comp.requestLayout();
							updateScrolledComposite();
						} catch (Exception e) {
							e.printStackTrace();
						}
					});

				} catch (Exception e) {
					// e.printStackTrace();
				}

				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	private void createLeftComp() {
		// left

		var parent = new Composite(_mainComp, SWT.NONE);
		parent.setLayout(new GridLayout(1, true));
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		createHSpace(parent);

		createHeader(parent, "Start");

		createLink(parent, "New Project", createWizardRunnable(new NewPhaserProjectWizard()), "new_phaser_project.png");
		createLink(parent, "New Example Project", createWizardRunnable(new NewPhaserExampleProjectWizard()),
				"new_phaser_project.png");

		createHSpace(parent);

		createHeader(parent, "Workspace");

		createOpenProjects(parent);
	}

	private static void createSocialChannelsComp(Composite parent) {
		var socialComp = new Composite(parent, SWT.NONE);
		var layout2 = new GridLayout(3, false);
		layout2.marginWidth = 0;
		layout2.marginHeight = 0;

		socialComp.setLayout(layout2);

		createLink(socialComp, "Twitter", createWebLinkRunnable("https://twitter.com/PhaserEditor2D"), "twitter.png");
		createLink(socialComp, "Facebook", createWebLinkRunnable("https://www.facebook.com/PhaserEditor2D"),
				"facebook.png");
		createLink(socialComp, "YouTube",
				createWebLinkRunnable("https://www.youtube.com/channel/UCQjNNvIWMiWGk2o3u8s67Dg"), "youtube.png");
		createLink(socialComp, "Itch", createWebLinkRunnable("https://phasereditor2d.itch.io/ide"), "itch.png");
		createLink(socialComp, "Reddit", createWebLinkRunnable("https://www.reddit.com/user/PhaserEditor2D"),
				"reddit.png");
		createLink(socialComp, "GitHub", createWebLinkRunnable("https://github.com/PhaserEditor2D/PhaserEditor"),
				"octoface.png");
	}

	private static Runnable createWebLinkRunnable(String url) {
		return () -> WebRunUI.openBrowser(url);
	}

	private static Runnable createWizardRunnable(IWizard wizard) {
		return new Runnable() {

			@Override
			public void run() {
				IWorkbench wb = PlatformUI.getWorkbench();
				var shell = wb.getActiveWorkbenchWindow().getShell();
				var dlg = new WizardDialog(shell, wizard);
				if (wizard instanceof INewWizard) {
					((INewWizard) wizard).init(wb, StructuredSelection.EMPTY);
				}
				dlg.open();
			}
		};
	}

	private static void createMainHeader(Composite comp) {
		var comp2 = new Composite(comp, SWT.NONE);
		comp2.setLayout(new GridLayout(1, true));
		comp2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

		var label = new Label(comp2, SWT.NONE);
		label.setText("Phaser Editor 2D");
		var font = label.getFont();
		var fd = font.getFontData()[0];
		label.setFont(SwtRM.getBoldFont(font));
		label.setFont(SwtRM.getFont(fd.name, fd.getHeight() * 2, fd.style));

		label = new Label(comp2, SWT.NONE);
		label.setText("A friendly IDE for HTML5 game development");
	}

	private static Label createLabel(Composite comp, String text) {
		var label = new Label(comp, SWT.NONE);
		label.setText(text);
		return label;
	}

	@SuppressWarnings("unused")
	private static void createHSpace(Composite comp) {
		new Label(comp, SWT.NONE);
	}

	private static void createOpenProjects(Composite comp) {
		for (var project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			createLink(comp, project.getName(), null,
					"platform:/plugin/org.eclipse.ui.ide/icons/full/obj16/prj_obj.png");
		}
	}

	private static void createHeader(Composite comp, String text) {
		var label = new Label(comp, SWT.NONE);
		label.setText(text);
		label.setFont(SwtRM.getBoldFont(label.getFont()));
		createHSpace(comp);
	}

	private static void createLink(Composite comp, String text, Runnable action) {
		createLink(comp, text, action, null);
	}

	private static void createLink(Composite comp, String text, Runnable action, String icon) {
		var label = new ImageHyperlink(comp, SWT.CENTER);
		label.setForeground(JFaceColors.getActiveHyperlinkText(comp.getDisplay()));
		label.setText(text);

		if (icon != null) {
			var path = icon.startsWith("platform") ? icon : "platform:/plugin/phasereditor.ui/icons/" + icon;
			label.setImage(EditorSharedImages.getImage(path));
		}

		label.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if (action != null) {
					action.run();
				}
			}
		});
	}

	@Override
	public void setFocus() {
		_mainComp.setFocus();
	}

}