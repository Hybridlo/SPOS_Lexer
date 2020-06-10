Sub Main()

On Error GoTo Failed

 

Dim app As Netica.Application

app = New Netica.Application

app.Visible = True

 

Dim net_file_name As String

net_file_name = System.AppDomain.CurrentDomain.BaseDirectory() & "..\..\..\ChestClinic.dne"

Dim net As Netica.Bnet

net = app.ReadBNet(app.NewStream(net_file_name))

net.Compile()

 

Dim TB As Netica.BNode

TB = net.Nodes.Item("Tuberculosis")

Dim belief As Double

belief = TB.GetBelief("present")

MsgBox("The probability of tuberculosis is " & belief)

 

net.Nodes.Item("XRay").EnterFinding("abnormal")

belief = TB.GetBelief("present")

MsgBox("Given an abnormal X-Ray, the probability of tuberculosis is " & belief)

 

net.Nodes.Item("VisitAsia").EnterFinding("visit")

belief = TB.GetBelief("present")

MsgBox("Given abnormal X-Ray and visit to Asia, the probability of tuberculosis is " & belief)

 

net.Nodes.Item("Cancer").EnterFinding("present")

belief = TB.GetBelief("present")

MsgBox("Given abnormal X-Ray, Asia visit, and lung cancer, the probability of tuberculosis is " & belief)

 

net.Delete()

If Not app.UserControl Then

app.Quit()

End If

 

Exit Sub

Failed:

 

MsgBox("NeticaDemo: Error " & (Err.Number And &H7FFFS) & ": " & Err.Description)

 

End Sub